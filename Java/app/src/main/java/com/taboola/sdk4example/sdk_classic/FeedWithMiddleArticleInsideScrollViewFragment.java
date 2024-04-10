package com.taboola.sdk4example.sdk_classic;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taboola.sdk4example.Const;
import com.taboola.sdk4example.FlagsConst;
import com.taboola.sdk4example.R;
import com.taboola.android.TBLClassicPage;
import com.taboola.android.TBLClassicUnit;
import com.taboola.android.Taboola;
import com.taboola.android.annotations.TBL_PLACEMENT_TYPE;
import com.taboola.android.listeners.TBLClassicListener;

import java.util.HashMap;


/**
 * This example shows the Middle Article Widget + Feed units in a ScrollView. The units created via XML.
 */
public class FeedWithMiddleArticleInsideScrollViewFragment extends Fragment {
    private static final String TAG = "FeedWithMiddleArticleInsideScrollViewFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_standard, container, false);

        // Create a Taboola page
        TBLClassicPage tblClassicPage = Taboola.getClassicPage(Const.PAGE_URL, Const.PAGE_TYPE);

        // Create Taboola units for the page
        TBLClassicUnit tblClassicUnitMiddleArticle = view.findViewById(R.id.taboola_widget_middle);
        TBLClassicUnit tblClassicUnitBelowArticle = view.findViewById(R.id.taboola_widget_below_article);

        // Configure and add the units the page
        configureMiddleArticleWidget(tblClassicUnitMiddleArticle, tblClassicPage);
        configureBelowArticleWidget(tblClassicUnitBelowArticle, tblClassicPage);

        // Fetch content for each unit
        tblClassicUnitMiddleArticle.fetchContent();
        tblClassicUnitBelowArticle.fetchContent();
        return view;
    }

    private void configureMiddleArticleWidget(TBLClassicUnit tblClassicUnit, TBLClassicPage tblClassicPage) {
        tblClassicUnit.setTargetType("mix");

        TBLClassicListener tblClassicListener = new TBLClassicListener() {
            @Override
            public boolean onItemClick(String placementName, String itemId, String clickUrl, boolean isOrganic, String customData) {
                return super.onItemClick(placementName, itemId, clickUrl, isOrganic, customData);
            }
        };
        tblClassicPage.addUnitToPage(tblClassicUnit,
                Const.WIDGET_MIDDLE_PLACEMENT_NAME,
                Const.WIDGET_MIDDLE_MODE,
                TBL_PLACEMENT_TYPE.FEED,
                tblClassicListener);
    }

    private void configureBelowArticleWidget(TBLClassicUnit tblClassicUnit, TBLClassicPage tblClassicPage) {
        tblClassicUnit.setTargetType("mix");

        HashMap<String, String> extraProperties = new HashMap<>();
        extraProperties.put(FlagsConst.USE_ONLINE_TEMPLATE, "true");
        extraProperties.put(FlagsConst.DETAILED_ERROR_CODES, "true");
        TBLClassicListener tblClassicListener = new TBLClassicListener() {
            @Override
            public boolean onItemClick(String placementName, String itemId, String clickUrl, boolean isOrganic, String customData) {
                return super.onItemClick(placementName, itemId, clickUrl, isOrganic, customData);
            }
        };

        tblClassicUnit.setUnitExtraProperties(extraProperties);
        tblClassicPage.addUnitToPage(tblClassicUnit, Const.FEED_PLACEMENT_NAME, Const.FEED_MODE, TBL_PLACEMENT_TYPE.FEED, tblClassicListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}