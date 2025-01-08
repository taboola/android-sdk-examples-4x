package com.taboola.sdk4example.sdk_classic;

import static com.taboola.sdk4example.Const.META_WIDGET_MODE;
import static com.taboola.sdk4example.Const.META_WIDGET_PLACEMENT_NAME;
import static com.taboola.sdk4example.MetaConst.AMOUNT_OF_LINES_BETWEEN_LINES;
import static com.taboola.sdk4example.MetaConst.ELEMENT_TYPE_BRANDING;
import static com.taboola.sdk4example.MetaConst.NUMBER_OF_LINES;
import static com.taboola.sdk4example.MetaConst.TEXT_FONT_SIZE;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.facebook.ads.NativeAdLayout;
import com.taboola.android.TBLClassicPage;
import com.taboola.android.TBLClassicUnit;
import com.taboola.android.TBLPublisherInfo;
import com.taboola.android.Taboola;
import com.taboola.android.annotations.TBL_PLACEMENT_TYPE;
import com.taboola.android.listeners.TBLClassicListener;

import com.taboola.android.utils.style_properties.TBLTitleStylePropertiesBuilder;
import com.taboola.android.utils.style_properties.TBLUiStyleProperties;
import com.taboola.android.utils.style_properties.TBLCallToActionButtonStylePropertiesBuilder;
import com.taboola.sdk4example.Const;
import com.taboola.sdk4example.MetaConst;
import com.taboola.sdk4example.R;
import com.taboola.sdk4example.tabs.BaseTaboolaFragment;
import com.taboola.android.utils.style_properties.TBLTextStylePropertiesBuilder;

import java.util.HashMap;

public class MetaAdUICustomization extends BaseTaboolaFragment {


    private static final String TAG = MetaAdUICustomization.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Taboola.init(new TBLPublisherInfo(MetaConst.META_PUBLISHER_NAME));
        View rootView = inflater.inflate(R.layout.fragment_meta_ad_classic_unit, null);
        NativeAdLayout adContainerTop = rootView.findViewById(R.id.native_ad_container_top);

        Taboola.setGlobalExtraProperties(new HashMap<String, String>() {{
            put(MetaConst.AUDIENCE_NETWORK_APPLICATION_ID_KEY, MetaConst.AUDIENCE_NETWORK_APP_ID);
            put(MetaConst.ENABLE_META_DEMAND_DEBUG_KEY, "true");
        }});

        setupAndLoadTaboolaAd(adContainerTop);
        return rootView;
    }


    private void setupAndLoadTaboolaAd(NativeAdLayout adContainer) {
        TBLClassicPage tblClassicPage = Taboola.getClassicPage(Const.PAGE_URL, Const.PAGE_TYPE);
        TBLClassicUnit tblClassicUnit = tblClassicPage.build(getContext(), META_WIDGET_PLACEMENT_NAME, META_WIDGET_MODE, TBL_PLACEMENT_TYPE.PAGE_MIDDLE, new TBLClassicListener() {
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
        });
        tblClassicUnit.setUnitExtraProperties(new HashMap<String, String>() {{
            put(MetaConst.AUDIENCE_NETWORK_PLACEMENT_ID_KEY, MetaConst.AUDIENCE_NETWORK_CAROUSEL_PLACEMENT_ID);
        }});
        TBLUiStyleProperties brandingStyleProperties = new TBLTextStylePropertiesBuilder(ELEMENT_TYPE_BRANDING)
                .setFontLightColor(R.color.design_default_color_error)
                .setFontDarkColor(R.color.colorPrimary)
                .setFontSize(TEXT_FONT_SIZE).
                build();
        TBLUiStyleProperties titleStyleProperties = new TBLTitleStylePropertiesBuilder()
                .setAmountOfSpaceBetweenLines(AMOUNT_OF_LINES_BETWEEN_LINES)
                .setLines(NUMBER_OF_LINES)
                .setFontLightColor(Color.RED)
                .setFontSize(TEXT_FONT_SIZE)
                .setFontDarkColor(Color.BLUE)
                .build();
        TBLUiStyleProperties ctaStyleProperties = new TBLCallToActionButtonStylePropertiesBuilder()
                .setVisibility(true)
                .build();
        tblClassicUnit.setNativeUI(MetaConst.DEFAULT_LAYOUT_KEY, brandingStyleProperties, titleStyleProperties, ctaStyleProperties);
        adContainer.addView(tblClassicUnit);
        tblClassicUnit.fetchContent();
    }


}
