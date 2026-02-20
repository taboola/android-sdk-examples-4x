package com.taboola.sdk4example.sdk_classic;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.taboola.android.TBLClassicPage;
import com.taboola.android.Taboola;
import com.taboola.android.listeners.TBLClassicInterstitialListener;
import com.taboola.sdk4example.Const;
import com.taboola.sdk4example.R;

/**
 * Shows a Taboola Classic interstitial flow.
 * <p>
 * This fragment initializes a Classic page and demonstrates how to load and present
 * a full-screen interstitial using the Taboola SDK.
 * <p>
 * Interstitial implementation overview:
 * <ul>
 *     <li>Creates a {@link TBLClassicPage} with the page URL/type from {@link Const}.</li>
 *     <li>Initializes the interstitial placement with placement name, mode, and custom segment.</li>
 *     <li>Loads the ad immediately; the loading UI is visible until {@code onInterstitialLoaded}.</li>
 *     <li>Enables the "show" button only after a successful load to avoid empty presentation.</li>
 *     <li>Presents the interstitial on user action and reacts to lifecycle callbacks
 *     (presented/dismissed/clicked) for UI updates and logging.</li>
 *     <li>If loading fails, the loading UI is hidden and the error is surfaced to the user.</li>
 * </ul>
 */
public class InterstitialFragment extends Fragment {

    private static final String TAG = InterstitialFragment.class.getSimpleName();
    private TBLClassicPage tblClassicPage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interstitial, container, false);
    }

    /**
     * Initializes the Taboola Classic page, sets up callbacks, and loads the interstitial.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout interstitialLoadingContainer = view.findViewById(R.id.interstitial_loading_container);
        Button showInterstitialButton = view.findViewById(R.id.show_interstitial_btn);

        tblClassicPage = Taboola.getClassicPage(Const.PAGE_URL, Const.PAGE_TYPE);
        TBLClassicInterstitialListener tblClassicInterstitialListener = new TBLClassicInterstitialListener() {
            @Override
            public void onInterstitialLoaded() {
                super.onInterstitialLoaded();
                interstitialLoadingContainer.setVisibility(View.GONE);
                showInterstitialButton.setVisibility(View.VISIBLE);
                Log.d(TAG, "The Interstitial is loaded successfully.");
            }

            @Override
            public void onInterstitialWillPresent() {
                super.onInterstitialWillPresent();
                Log.d(TAG, "The Interstitial will be presented.");
            }

            @Override
            public void onInterstitialPresented() {
                super.onInterstitialPresented();
                showInterstitialButton.setVisibility(View.GONE);
                Log.d(TAG, "The Interstitial is presented.");
            }

            @Override
            public void onInterstitialWillDismiss() {
                super.onInterstitialWillDismiss();
                Log.d(TAG, "The Interstitial will be dismissed.");
            }

            @Override
            public void onInterstitialDismissed() {
                super.onInterstitialDismissed();
                Log.d(TAG, "The Interstitial is dismissed.");
            }

            @Override
            public void onInterstitialClicked() {
                super.onInterstitialClicked();
                Log.d(TAG, "The Interstitial is clicked.");
            }

            @Override
            public void interstitialDidFailToLoadAdWithError(String error) {
                super.interstitialDidFailToLoadAdWithError(error);
                interstitialLoadingContainer.setVisibility(View.GONE);
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                Log.d(TAG, error);
            }
        };

        tblClassicPage.initInterstitial(
                Const.INTERSTITIAL_PLACEMENT_NAME,
                Const.INTERSTITIAL_MODE,
                Const.INTERSTITIAL_CUSTOM_SEGMENT_DEFAULT,
                tblClassicInterstitialListener
        );

        tblClassicPage.loadInterstitial();

        showInterstitialButton.setOnClickListener(v -> {
            tblClassicPage.showInterstitial();
        });
    }
}

