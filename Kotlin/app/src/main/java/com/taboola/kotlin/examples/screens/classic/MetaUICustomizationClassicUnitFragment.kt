package com.taboola.kotlin.examples.screens.classic

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.fragment.app.Fragment
import com.facebook.ads.NativeAdLayout
import com.taboola.android.MetaPlacementProperties
import com.taboola.android.TBLPublisherInfo
import com.taboola.android.Taboola
import com.taboola.android.annotations.TBL_PLACEMENT_TYPE
import com.taboola.android.listeners.TBLClassicListener
import com.taboola.android.utils.style_properties.TBLCallToActionButtonStylePropertiesBuilder
import com.taboola.android.utils.style_properties.TBLTextStylePropertiesBuilder
import com.taboola.android.utils.style_properties.TBLTitleStylePropertiesBuilder
import com.taboola.kotlin.examples.MetaConst
import com.taboola.kotlin.examples.MetaConst.Companion.AMOUNT_OF_SPACE_BETWEEN_ELEMENTS
import com.taboola.kotlin.examples.MetaConst.Companion.DARK_MODE
import com.taboola.kotlin.examples.MetaConst.Companion.ELEMENT_TYPE_BRANDING
import com.taboola.kotlin.examples.MetaConst.Companion.FONT_TYPEFACE_ARIAL_BOLD
import com.taboola.kotlin.examples.MetaConst.Companion.NUMBER_OF_LINES
import com.taboola.kotlin.examples.MetaConst.Companion.TEXT_FONT_SIZE
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R
import com.taboola.kotlin.examples.screens.utils.Utils.Companion.getFullPathToFontFile

class MetaUICustomizationClassicUnitFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Taboola.init(TBLPublisherInfo(MetaConst.META_PUBLISHER_NAME))
        val rootView = inflater.inflate(R.layout.fragment_meta_ad_classic_unit, null)
        val adContainerTop: NativeAdLayout = rootView.findViewById(R.id.native_ad_container_top)
        Taboola.setGlobalExtraProperties(object : HashMap<String?, String?>() {
            init {

                val isDarkTheme = isDarkTheme(requireActivity())
                put(
                    MetaConst.AUDIENCE_NETWORK_APPLICATION_ID_KEY, MetaConst.AUDIENCE_NETWORK_APP_ID
                )
                put(MetaConst.ENABLE_META_DEMAND_DEBUG_KEY, "true")

                //if the device is in dark mode, set the dark mode flag
                if (isDarkTheme) {
                    put(DARK_MODE, "true")
                }
            }
        })
        setupAndLoadTaboolaAd(adContainerTop)
        return rootView
    }

    // check OS dark mode
    fun isDarkTheme(activity: Activity): Boolean {
        return activity.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }


    private fun setupAndLoadTaboolaAd(adContainer: NativeAdLayout) {
        val widgetProperties = PlacementInfo.metaWidgetProperties()
        val feedProperties = PlacementInfo.metaFeedProperties()
        val tblClassicPage = Taboola.getClassicPage(feedProperties.pageUrl, feedProperties.pageType)
        val metaPlacementProperties =
            MetaPlacementProperties(widgetProperties.placementName, widgetProperties.mode)
        val tblMetaClassicUnit = tblClassicPage.buildWithMeta(context,
            feedProperties.placementName,
            feedProperties.mode,
            TBL_PLACEMENT_TYPE.PAGE_MIDDLE,
            metaPlacementProperties,
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

                override fun onResize(height: Int) {
                    super.onResize(height)
                    Log.d(TAG, "onResize $height")
                }
            })
        //Force the ad to be of type image_link
        tblMetaClassicUnit.setMetaAdTypeForDebug(MetaConst.TEST_LAYOUT_IMAGE_LINK_TYPE)
        tblMetaClassicUnit.setUnitExtraProperties(hashMapOf(MetaConst.AUDIENCE_NETWORK_PLACEMENT_ID_KEY to MetaConst.AUDIENCE_NETWORK_PLACEMENT_ID))

        val typefaceArielBold = Typeface.Builder(
            requireActivity().assets,
            getFullPathToFontFile(FONT_TYPEFACE_ARIAL_BOLD)

        ).build()

        // Create custom style properties for the title
        val titleStyleProperties = TBLTitleStylePropertiesBuilder()
            .setAmountOfSpaceBetweenLines(AMOUNT_OF_SPACE_BETWEEN_ELEMENTS)
            .setLines(NUMBER_OF_LINES)
            .setFontSize(TEXT_FONT_SIZE)
            .setFontLightColor(Color.Blue.toArgb())
            .setFontDarkColor(Color.Red.toArgb())
            .setTypeface(typefaceArielBold)
            .build()

        // Create custom style properties for the branding
        val brandingStyleProperties =
            TBLTextStylePropertiesBuilder(ELEMENT_TYPE_BRANDING)
                .setFontSize(TEXT_FONT_SIZE)
                .setFontLightColor(R.color.purple_200)
                .setFontDarkColor(R.color.purple_500)
                .setTypeface(typefaceArielBold)
                .build()

//        // Create custom style properties for the call to action button
//        val callToActionButtonStyleProperties =
//            TBLCallToActionButtonStylePropertiesBuilder()
//                // Sets the visibility of the call-to-action (CTA) button.
//                // The button is visible by default. for hide the button pass false.
//                .setVisibility(false)
//                .build()

        // Set the custom UI properties to the Meta native Ad
        tblMetaClassicUnit.setMetaNativeUI(
            MetaConst.DEFAULT_LAYOUT_KEY,
            brandingStyleProperties,
            titleStyleProperties,
//            callToActionButtonStyleProperties
        )
        adContainer.addView(tblMetaClassicUnit)
        tblMetaClassicUnit.fetchContent()
    }

    companion object {
        private val TAG = MetaUICustomizationClassicUnitFragment::class.java.simpleName
    }
}


