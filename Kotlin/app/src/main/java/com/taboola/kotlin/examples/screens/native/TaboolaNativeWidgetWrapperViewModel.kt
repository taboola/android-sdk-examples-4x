package com.taboola.kotlin.examples.screens.native

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.taboola.android.TBLPublisherInfo
import com.taboola.android.Taboola
import com.taboola.android.listeners.TBLNativeListener
import com.taboola.android.tblnative.*
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.PublisherInfo

class TaboolaNativeWidgetWrapperViewModel : ViewModel() {
    private val tblNativeUnit: TBLNativeUnit
    private var image: TBLImageView? by mutableStateOf(null)
    private var title: TBLTextView? by mutableStateOf(null)
    private var placementProperties = PlacementInfo.widgetProperties()

    fun getTBLImage(): TBLImageView? = image
    fun getTBLTitle(): TBLTextView? = title

    init {
        tblNativeUnit = getTaboolaUnit(placementProperties)
    }

    /**
     * Define a Page that represents this screen, get a Unit from it, add it to screen and fetch its content
     */
    private fun getTaboolaUnit(properties: PlacementInfo.WidgetProperties): TBLNativeUnit {
        // Define a page to control all Unit placements on this screen
        val nativePage: TBLNativePage =
            Taboola.getNativePage(properties.sourceType, properties.pageUrl)

        // Define a publisher info with publisher name and api key
        val tblPublisherInfo =
            TBLPublisherInfo(PublisherInfo.PUBLISHER).setApiKey(PublisherInfo.API_KEY)

        // Define a fetch request (with desired number of content items in setRecCount())
        val requestData = TBLRequestData().setRecCount(1)

        // Define a single Unit to display
        val nativeUnit: TBLNativeUnit = nativePage.build(
            properties.placementName,
            tblPublisherInfo,
            requestData,
            object : TBLNativeListener() {
                override fun onItemClick(
                    placementName: String?,
                    itemId: String?,
                    clickUrl: String?,
                    isOrganic: Boolean,
                    customData: String?
                ): Boolean {
                    println("Taboola | onItemClick | isOrganic = $isOrganic")
                    return super.onItemClick(placementName, itemId, clickUrl, isOrganic, customData)
                }
            })

        return nativeUnit
    }

    fun fetchRecommendation(context: Context) {

        tblNativeUnit.fetchRecommendations(object : TBLRecommendationRequestCallback {
            override fun onRecommendationsFetched(recommendationsResponse: TBLRecommendationsResponse?) {
                println("Taboola | onRecommendationsFetched")

                // Add Unit content to layout
                if (recommendationsResponse == null) {
                    println("Error: No recommendations returned from server.")
                    return
                }

                val item: TBLRecommendationItem? =
                    recommendationsResponse.placementsMap[placementProperties.placementName]?.items?.get(
                        0
                    )

                if (item == null) {
                    println("Error: No such item returned from server.")
                    return
                }

                // Extract Taboola Views from item
                try {
                    val thumbnailView = item.getThumbnailView(context)
                    val titleView = item.getTitleView(context)

                    image = thumbnailView
                    title = titleView
                } catch (exception: IllegalStateException) {
                    println("Fragment Context no longer valid, not rendering Taboola UI.")
                }
            }

            override fun onRecommendationsFailed(throwable: Throwable?) {
                println("Taboola | onRecommendationsFailed: ${throwable?.message}")
            }
        })
    }
}