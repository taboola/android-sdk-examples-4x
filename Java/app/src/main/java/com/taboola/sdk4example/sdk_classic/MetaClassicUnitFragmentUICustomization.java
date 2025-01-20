package com.taboola.sdk4example.sdk_classic;

import static com.taboola.sdk4example.Const.META_FEED_MODE;
import static com.taboola.sdk4example.Const.META_FEED_PLACEMENT_NAME;
import static com.taboola.sdk4example.Const.META_WIDGET_MODE;
import static com.taboola.sdk4example.Const.META_WIDGET_PLACEMENT_NAME;
import static com.taboola.sdk4example.MetaConst.AMOUNT_OF_SPACE_BETWEEN_LINES;
import static com.taboola.sdk4example.MetaConst.DARK_NODE;
import static com.taboola.sdk4example.MetaConst.ELEMENT_TYPE_BRANDING;
import static com.taboola.sdk4example.MetaConst.NUMBER_OF_LINES;
import static com.taboola.sdk4example.MetaConst.TEXT_FONT_SIZE;
import static com.taboola.sdk4example.MetaConst.TYPEFACE_ARIAL_BOLD;
import static com.taboola.sdk4example.utils.Utils.loadFont;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.facebook.ads.NativeAdLayout;
import com.taboola.android.MetaPlacementProperties;
import com.taboola.android.TBLClassicPage;
import com.taboola.android.TBLMetaClassicUnit;
import com.taboola.android.TBLPublisherInfo;
import com.taboola.android.Taboola;
import com.taboola.android.annotations.TBL_PLACEMENT_TYPE;
import com.taboola.android.listeners.TBLClassicListener;
import com.taboola.android.utils.style_properties.TBLCallToActionButtonStylePropertiesBuilder;
import com.taboola.android.utils.style_properties.TBLTextStylePropertiesBuilder;
import com.taboola.android.utils.style_properties.TBLTitleStylePropertiesBuilder;
import com.taboola.android.utils.style_properties.TBLUiStyleProperties;
import com.taboola.sdk4example.Const;
import com.taboola.sdk4example.MetaConst;
import com.taboola.sdk4example.R;
import com.taboola.sdk4example.tabs.BaseTaboolaFragment;

import java.util.HashMap;

public class MetaClassicUnitFragmentUICustomization extends BaseTaboolaFragment {

    private static final String TAG = MetaClassicUnitFragmentUICustomization.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        int isOSDarkMode =
                getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        Taboola.init(new TBLPublisherInfo(MetaConst.META_PUBLISHER_NAME));
        View rootView = inflater.inflate(R.layout.fragment_meta_ad_classic_unit, null);
        NativeAdLayout adContainerTop = rootView.findViewById(R.id.native_ad_container_top);

        Taboola.setGlobalExtraProperties(new HashMap<String, String>() {{
            put(MetaConst.AUDIENCE_NETWORK_APPLICATION_ID_KEY, MetaConst.AUDIENCE_NETWORK_APP_ID);
            put(MetaConst.ENABLE_META_DEMAND_DEBUG_KEY, "true");
            if (isOSDarkMode == Configuration.UI_MODE_NIGHT_YES) {
                put(DARK_NODE, "true");
            }
        }});

        setupAndLoadTaboolaAd(adContainerTop);
        return rootView;
    }


    private void setupAndLoadTaboolaAd(NativeAdLayout adContainer) {
        TBLClassicPage tblClassicPage = Taboola.getClassicPage(Const.PAGE_URL, Const.PAGE_TYPE);
        MetaPlacementProperties metaPlacementProperties = new MetaPlacementProperties(META_WIDGET_PLACEMENT_NAME, META_WIDGET_MODE);
        TBLMetaClassicUnit tblMetaClassicUnit = tblClassicPage.buildWithMeta(getContext(), META_FEED_PLACEMENT_NAME, META_FEED_MODE, TBL_PLACEMENT_TYPE.PAGE_MIDDLE, metaPlacementProperties, new TBLClassicListener() {
            @Override
            public boolean onItemClick(String placementName, String itemId, String clickUrl, boolean isOrganic, String customData) {
                Log.d(TAG, "onItemClick");
                return super.onItemClick(placementName, itemId, clickUrl, isOrganic, customData);
            }

            @Override
            public void onAdReceiveSuccess() {
                super.onAdReceiveSuccess();
                Log.d(TAG, "onAdReceiveSuccess");
            }

            @Override
            public void onAdReceiveFail(String error) {
                super.onAdReceiveFail(error);
                Log.d(TAG, "onAdReceiveFail " + error);
            }

            @Override
            public void onResize(int height) {
                super.onResize(height);
                Log.d(TAG, "onResize");
            }
        });
        tblMetaClassicUnit.setMetaAdTypeForDebug(MetaConst.TEST_LAYOUT_IMAGE_LINK_TYPE);
        tblMetaClassicUnit.setUnitExtraProperties(new HashMap<String, String>() {{
            put(MetaConst.AUDIENCE_NETWORK_PLACEMENT_ID_KEY, MetaConst.AUDIENCE_NETWORK_PLACEMENT_ID);
        }});

        Typeface font = loadFont(getActivity(), TYPEFACE_ARIAL_BOLD);

        // Create custom style properties for the branding
        TBLUiStyleProperties brandingStyleProperties = new TBLTextStylePropertiesBuilder(ELEMENT_TYPE_BRANDING)
                .setFontLightColor(R.color.design_default_color_error)
                .setFontDarkColor(R.color.colorPrimary)
                .setFontSize(TEXT_FONT_SIZE)
                .setTypeface(font)
                .build();

        // Create custom style properties for the title
        TBLUiStyleProperties titleStyleProperties = new TBLTitleStylePropertiesBuilder()
                .setAmountOfSpaceBetweenLines(AMOUNT_OF_SPACE_BETWEEN_LINES)
                .setLines(NUMBER_OF_LINES)
                .setFontLightColor(Color.RED)
                .setFontSize(TEXT_FONT_SIZE)
                .setFontDarkColor(Color.BLUE)
                .setTypeface(font)
                .build();

        // Create custom style properties for the call to action button
//        TBLUiStyleProperties ctaStyleProperties = new TBLCallToActionButtonStylePropertiesBuilder()
//                // Sets the visibility of the call-to-action (CTA) button.
//                // The button is visible by default. for hide the button pass false.
//                .setVisibility(false)
//                .build();

        // Set the custom UI properties to the Meta native Ad
        tblMetaClassicUnit.setMetaNativeUI(MetaConst.DEFAULT_LAYOUT_KEY,
                brandingStyleProperties,
                titleStyleProperties
//                ctaStyleProperties
        );
        adContainer.addView(tblMetaClassicUnit);
        tblMetaClassicUnit.fetchContent();
    }

}
