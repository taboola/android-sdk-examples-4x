package com.taboola.kotlin.examples.screens.classic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.ads.NativeAdLayout
import com.taboola.android.MetaPlacementProperties
import com.taboola.android.TBLPublisherInfo
import com.taboola.android.Taboola
import com.taboola.android.annotations.TBL_PLACEMENT_TYPE
import com.taboola.android.listeners.TBLClassicListener
import com.taboola.kotlin.examples.MetaConst
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R

class MetaClassicUnitFragment : Fragment() {

    private var rootView: View? = null
    private var adContainerTop: NativeAdLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Taboola.init(TBLPublisherInfo(MetaConst.META_PUBLISHER_NAME))
        rootView = inflater.inflate(R.layout.fragment_meta_ad_classic_unit, null)
        adContainerTop = rootView!!.findViewById<NativeAdLayout>(R.id.native_ad_container_top)
        Taboola.setGlobalExtraProperties(object : HashMap<String?, String?>() {
            init {
                put(MetaConst.AUDIENCE_NETWORK_APPLICATION_ID_KEY, MetaConst.AUDIENCE_NETWORK_APP_ID)
                put(MetaConst.ENABLE_META_DEMAND_DEBUG_KEY, "true")
            }
        })
        adContainerTop?.let { setupAndLoadTaboolaAd(it) }
        return rootView
    }


    private fun setupAndLoadTaboolaAd(adContainer: NativeAdLayout) {
        val widgetProperties = PlacementInfo.metaWidgetProperties()
        val feedProperties = PlacementInfo.metaFeedProperties()
        val tblClassicPage = Taboola.getClassicPage(feedProperties.pageUrl, feedProperties.pageType)
        val metaPlacementProperties =
            MetaPlacementProperties(widgetProperties.placementName, widgetProperties.mode)
        val tblMetaClassicUnit = tblClassicPage.buildWithMeta(
            context,
            feedProperties.placementName,
            feedProperties.mode,
            TBL_PLACEMENT_TYPE.PAGE_MIDDLE,
            metaPlacementProperties,
            object : TBLClassicListener() {})
        tblMetaClassicUnit.setMetaAdTypeForDebug(MetaConst.TEST_LAYOUT_TYPE)
        tblMetaClassicUnit.setUnitExtraProperties(object : HashMap<String?, String?>() {
            init {
                put(MetaConst.AUDIENCE_NETWORK_PLACEMENT_ID_KEY, MetaConst.AUDIENCE_NETWORK_PLACEMENT_ID)
            }
        })
        tblMetaClassicUnit.setMetaNativeUI(MetaConst.DEFAULT_LAYOUT_KEY)
        adContainer.addView(tblMetaClassicUnit)
        tblMetaClassicUnit.fetchContent()
    }
}


