package com.taboola.kotlin.examples.screens.classic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.ads.NativeAdLayout
import com.taboola.android.TBLPublisherInfo
import com.taboola.android.Taboola
import com.taboola.android.annotations.TBL_PLACEMENT_TYPE
import com.taboola.android.listeners.TBLClassicListener
import com.taboola.kotlin.examples.MetaConst
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R

class MetaAdInsideScrollViewFragment : Fragment() {

    private var rootView: View? = null
    private var adContainerTop: NativeAdLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Taboola.init(TBLPublisherInfo(MetaConst.META_PUBLISHER_NAME))
        rootView = inflater.inflate(R.layout.fragment_meta_ad_inside_sv, null)
        adContainerTop = rootView!!.findViewById(R.id.native_ad_container_top)
        Taboola.setGlobalExtraProperties(object : HashMap<String?, String?>() {
            init {
                put(
                    MetaConst.AUDIENCE_NETWORK_APPLICATION_ID_KEY,
                    MetaConst.AUDIENCE_NETWORK_APP_ID
                )
                put(MetaConst.ENABLE_META_DEMAND_DEBUG_KEY, "true")
            }
        })
        adContainerTop?.let { setupAndLoadTaboolaAd(it) }
        return rootView
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
            object : TBLClassicListener() {
                override fun onItemClick(
                    placementName: String?,
                    itemId: String?,
                    clickUrl: String?,
                    isOrganic: Boolean,
                    customData: String?
                ): Boolean {
                    Log.d(TAG, "onItemClick")
                    return super.onItemClick(placementName, itemId, clickUrl, isOrganic, customData)
                }

                override fun onAdReceiveSuccess() {
                    super.onAdReceiveSuccess()
                    Log.d(TAG, "onAdReceiveSuccess")
                }

                override fun onAdReceiveFail(error: String?) {
                    super.onAdReceiveFail(error)
                    Log.d(TAG, "onAdReceiveFail $error")
                }
            })
        tblClassicUnit.setAdTypeForDebug(MetaConst.TEST_LAYOUT_TYPE)
        tblClassicUnit.setUnitExtraProperties(object : java.util.HashMap<String?, String?>() {
            init {
                put(
                    MetaConst.AUDIENCE_NETWORK_PLACEMENT_ID_KEY,
                    MetaConst.AUDIENCE_NETWORK_PLACEMENT_ID
                )
            }
        })
        tblClassicUnit.setNativeUI(MetaConst.DEFAULT_LAYOUT_KEY)
        adContainer.addView(tblClassicUnit)
        tblClassicUnit.fetchContent()
    }

    companion object {
        private val TAG = MetaAdInsideScrollViewFragment::class.java.simpleName
    }
}
