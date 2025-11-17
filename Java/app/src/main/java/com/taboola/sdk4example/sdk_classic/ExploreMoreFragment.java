package com.taboola.sdk4example.sdk_classic;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;

import com.taboola.android.listeners.TBLExploreMoreClassicListener;
import com.taboola.sdk4example.Const;
import com.taboola.sdk4example.R;
import com.taboola.sdk4example.tabs.BaseTaboolaFragment;
import com.taboola.android.TBLClassicPage;
import com.taboola.android.Taboola;

/**
 * A fragment demonstrating the integration of the Taboola SDK's Explore More feature.
 * <p>
 * This fragment initializes a Taboola Classic Page and implements a custom back press handler
 * that conditionally displays the "Explore More" screen upon a system back button press,
 * allowing users to view more content before exiting the view.
 * <p>
 * The fragment keeps track of Explore More status and it will show Explore More
 * only if it has been loaded and has not been shown
 */
public class ExploreMoreFragment extends BaseTaboolaFragment {
    private TBLClassicPage tblClassicPage;
    private boolean shouldShowExploreMore = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_explore_more, container, false);

        tblClassicPage = Taboola.getClassicPage(Const.PAGE_URL, Const.PAGE_TYPE);

        initExploreMore(inflater.getContext());
        setUpBackPressedHandler();

        return view;
    }

    /**
     * Configures the Explore More feature.
     * <p>
     * Sets up a listener to track when the Explore More content is successfully received
     * and when the screen is opened, updating the {@code shouldShowExploreMore} flag accordingly.
     *
     * @param context The application or activity context required for Taboola initialization.
     */
    private void initExploreMore(Context context) {

        TBLExploreMoreClassicListener tblExploreMoreClassicListener;
        tblExploreMoreClassicListener = new TBLExploreMoreClassicListener() {
            @Override
            public void onAdReceiveSuccess() {
                super.onAdReceiveSuccess();
                shouldShowExploreMore = true;
            }

            @Override
            public void exploreMoreDidOpen() {
                super.exploreMoreDidOpen();
                shouldShowExploreMore = false;
            }
        };

        tblClassicPage.initExploreMore(
                context,
                tblExploreMoreClassicListener,
                Const.EXPLORE_MORE_PLACEMENT_NAME,
                Const.FEED_MODE,
                Const.EXPLORE_MORE_CUSTOM_SEGMENT_SUBSCRIBER);
    }

    /**
     * Sets up the handler to intercept the system's back button press.
     * If the condition 'shouldShowExploreMore' is true, it displays the Explore More screen instead of navigating back.
     * Note that Explore More can also be manually triggered by any other action.
     */
    private void setUpBackPressedHandler() {
        // Create an OnBackPressedCallback object
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Check if Explore More should be displayed
                if (shouldShowExploreMore) {
                    // If true, call showExploreMore
                    tblClassicPage.showExploreMore(requireActivity().getSupportFragmentManager());
                } else {
                    // If false, disable this callback and manually trigger the system's back press
                    setEnabled(false);
                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            }
        };

        // Add the callback to the activity's back press dispatcher
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
}
