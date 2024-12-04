package com.taboola.kotlin.examples.screens.classic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.ads.NativeAdLayout
import com.taboola.android.TBLPublisherInfo
import com.taboola.android.Taboola
import com.taboola.android.annotations.TBL_PLACEMENT_TYPE
import com.taboola.android.listeners.TBLClassicListener
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R

class MetaAdInsideScrollViewFragment : Fragment() {

    private var mRootView: View? = null
    private var adContainerTop: NativeAdLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Taboola.init(TBLPublisherInfo(META_PUBLISHER_NAME))
        mRootView = inflater.inflate(R.layout.fragment_meta_ad_inside_sv, null)
        adContainerTop = mRootView!!.findViewById(R.id.native_ad_container_top)
        Taboola.setGlobalExtraProperties(object : HashMap<String?, String?>() {
            init {
                put(AUDIENCE_NETWORK_APPLICATION_ID_KEY, AUDIENCE_NETWORK_APP_ID)
                put(ENABLE_META_DEMAND_DEBUG_KEY, "true")
            }
        })
        adContainerTop?.let { setupAndLoadTaboolaAd(it) }
        return mRootView
    }


    private fun setupAndLoadTaboolaAd(adContainer: NativeAdLayout) {
        val widgetProperties = PlacementInfo.metaWidgetProperties()
        val tblClassicPage =
            Taboola.getClassicPage(widgetProperties.pageUrl, widgetProperties.pageType)
        val tblClassicUnit = tblClassicPage.build(
            context,
            widgetProperties.placementName,
            widgetProperties.mode,
            TBL_PLACEMENT_TYPE.PAGE_MIDDLE,
            object : TBLClassicListener() {})
        tblClassicUnit.setAdTypeForDebug(TEST_LAYOUT_TYPE)
        tblClassicUnit.setUnitExtraProperties(object : java.util.HashMap<String?, String?>() {
            init {
                put(
                    AUDIENCE_NETWORK_PLACEMENT_ID_KEY,
                    AUDIENCE_NETWORK_PLACEMENT_ID
                )
            }
        })
        tblClassicUnit.setNativeUI(DEFAULT_LAYOUT_KEY)
        adContainer.addView(tblClassicUnit)
        tblClassicUnit.fetchContent()
    }
}

private const val META_PUBLISHER_NAME = "sdk-tester-meta"
private const val AUDIENCE_NETWORK_APP_ID = "1097593608162039"
private const val AUDIENCE_NETWORK_PLACEMENT_ID = "1097593608162039_1097982098123190"
private const val AUDIENCE_NETWORK_APPLICATION_ID_KEY = "audienceNetworkApplicationId"
private const val AUDIENCE_NETWORK_PLACEMENT_ID_KEY = "audienceNetworkPlacementId"
private const val ENABLE_META_DEMAND_DEBUG_KEY = "enableMetaDemandDebug"
private const val DEFAULT_LAYOUT_KEY = "default"
private const val TEST_LAYOUT_TYPE = "image_link"