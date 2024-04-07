package com.taboola.kotlin.examples.screens.native

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.taboola.android.TBLPublisherInfo
import com.taboola.android.Taboola
import com.taboola.android.listeners.TBLNativeListener
import com.taboola.android.tblnative.*
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.PublisherInfo
import com.taboola.kotlin.examples.R

class WidgetFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_native, container, false)
        val contentLayout = root.findViewById<LinearLayout>(R.id.content_layout)

        // Define Placement properties
        val placementProperties = PlacementInfo.widgetProperties()

        // Create and return a Taboola Unit
        val nativeUnit = getTaboolaUnit(placementProperties)

        // Fetch content for Unit
        nativeUnit.fetchRecommendations(object : TBLRecommendationRequestCallback {
            override fun onRecommendationsFetched(recommendationsResponse: TBLRecommendationsResponse?) {
                println("Taboola | onRecommendationsFetched")

                // Add Unit content to layout
                displayContent(contentLayout, recommendationsResponse, placementProperties)
            }

            override fun onRecommendationsFailed(throwable: Throwable?) {
                println("Taboola | onRecommendationsFailed: ${throwable?.message}")
            }
        })

        return root
    }

    /**
     * In Native integrations fetch request returns multiple Android Views.
     * It is up to you to lay them out.
     */
    private fun displayContent(
        contentLayout: LinearLayout,
        recommendationsResponse: TBLRecommendationsResponse?,
        placementProperties: PlacementInfo.WidgetProperties
    ) {
        if (recommendationsResponse == null) {
            println("Error: No recommendations returned from server.")
            return
        }

        // We asked for 1 item so we take the first item returned from Taboola
        // NOTE: In this example, we search for the items relevant for the only placement in this page
        val item: TBLRecommendationItem? =
            recommendationsResponse.placementsMap[placementProperties.placementName]?.items?.get(0)

        if (item == null) {
            println("Error: No such item returned from server.")
            return
        }

        // Extract Taboola Views from item
        try {
            val fragmentContext = requireContext()
            val thumbnailView = item.getThumbnailView(fragmentContext)
            val titleView = item.getTitleView(fragmentContext)

            contentLayout.addView(thumbnailView)
            contentLayout.addView(titleView)
        } catch (exception: IllegalStateException) {
            println("Fragment Context no longer valid, not rendering Taboola UI.")
        }
    }

    /**
     * Define a Page that represents this screen, get a Unit from it, add it to screen and fetch its content
     * Notice: A Unit of unlimited items, called "Feed" in Taboola, can be set in TBL_PLACEMENT_TYPE.PAGE_BOTTOM only.
     */
    private fun getTaboolaUnit(properties: PlacementInfo.WidgetProperties): TBLNativeUnit {
        // Define a page to control all Unit placements on this screen
        val nativePage: TBLNativePage =
            Taboola.getNativePage(properties.sourceType, properties.pageUrl)

        // Define a publisher info with publisher name and api key
        val tblPublisherInfo = TBLPublisherInfo(PublisherInfo.PUBLISHER).setApiKey(PublisherInfo.API_KEY)

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
}