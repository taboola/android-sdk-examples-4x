package com.taboola.sdk4example.sdk_classic;

import static com.taboola.sdk4example.Const.META_FEED_MODE;
import static com.taboola.sdk4example.Const.META_FEED_PLACEMENT_NAME;
import static com.taboola.sdk4example.Const.META_WIDGET_MODE;
import static com.taboola.sdk4example.Const.META_WIDGET_PLACEMENT_NAME;

import android.os.Bundle;
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
import com.taboola.sdk4example.Const;
import com.taboola.sdk4example.R;
import com.taboola.sdk4example.tabs.BaseTaboolaFragment;

import java.util.HashMap;

public class MetaClassicUnitFragment extends BaseTaboolaFragment {

    private View mRootView;
    private NativeAdLayout adContainerTop;

    private static final String META_PUBLISHER_NAME = "sdk-tester-meta";
    private static final String AUDIENCE_NETWORK_APP_ID = "1097593608162039";
    private static final String AUDIENCE_NETWORK_PLACEMENT_ID = "1097593608162039_1097982098123190";
    private static final String AUDIENCE_NETWORK_APPLICATION_ID_KEY = "audienceNetworkApplicationId";
    private static final String AUDIENCE_NETWORK_PLACEMENT_ID_KEY = "audienceNetworkPlacementId";
    private static final String ENABLE_META_DEMAND_DEBUG_KEY = "enableMetaDemandDebug";
    private static final String DEFAULT_LAYOUT_KEY = "default";
    private static final String TEST_LAYOUT_TYPE = "image_link";

    private static final String TAG = MetaClassicUnitFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Taboola.init(new TBLPublisherInfo(META_PUBLISHER_NAME));
        mRootView = inflater.inflate(R.layout.fragment_meta_ad_classic_unit, null);
        adContainerTop = mRootView.findViewById(R.id.native_ad_container_top);

        Taboola.setGlobalExtraProperties(new HashMap<String, String>() {{
            put(AUDIENCE_NETWORK_APPLICATION_ID_KEY, AUDIENCE_NETWORK_APP_ID);
            put(ENABLE_META_DEMAND_DEBUG_KEY, "true");
        }});

        setupAndLoadTaboolaAdAndFeed(adContainerTop);
        return mRootView;
    }


    private void setupAndLoadTaboolaAdAndFeed(NativeAdLayout adContainer) {
        TBLClassicPage tblClassicPage = Taboola.getClassicPage(Const.PAGE_URL, Const.PAGE_TYPE);
        MetaPlacementProperties metaPlacementProperties = new MetaPlacementProperties(META_WIDGET_PLACEMENT_NAME, META_WIDGET_MODE);
        TBLMetaClassicUnit tblMetaClassicUnit = tblClassicPage.buildWithMeta(getContext(), META_FEED_PLACEMENT_NAME, META_FEED_MODE, TBL_PLACEMENT_TYPE.PAGE_MIDDLE, metaPlacementProperties, new TBLClassicListener() {
        });
        tblMetaClassicUnit.setMetaAdTypeForDebug(TEST_LAYOUT_TYPE);
        tblMetaClassicUnit.setUnitExtraProperties(new HashMap<String, String>() {{
            put(AUDIENCE_NETWORK_PLACEMENT_ID_KEY, AUDIENCE_NETWORK_PLACEMENT_ID);
        }});

        tblMetaClassicUnit.setMetaNativeUI(DEFAULT_LAYOUT_KEY);
        adContainer.addView(tblMetaClassicUnit);
        tblMetaClassicUnit.fetchContent();
    }

}
