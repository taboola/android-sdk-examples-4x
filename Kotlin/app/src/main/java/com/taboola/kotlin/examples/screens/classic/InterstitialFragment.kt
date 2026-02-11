package com.taboola.kotlin.examples.screens.classic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.taboola.android.TBLClassicPage
import com.taboola.android.Taboola
import com.taboola.android.listeners.TBLClassicInterstitialListener
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R

/**
 * Shows a Taboola Classic interstitial flow.
 *
 * This fragment initializes a Classic page and demonstrates how to load and present
 * a full-screen interstitial using the Taboola SDK.
 *
 * Interstitial implementation overview:
 * - Creates a [TBLClassicPage] with the page URL/type from [PlacementInfo].
 * - Initializes the interstitial placement with placement name, mode, and custom segment.
 * - Loads the ad immediately; the loading UI is visible until
 *   [TBLClassicInterstitialListener.onInterstitialLoaded].
 * - Enables the "show" button only after a successful load to avoid empty presentation.
 * - Presents the interstitial on user action and reacts to lifecycle callbacks
 *   (presented/dismissed/clicked) for UI updates and logging.
 * - If loading fails, the loading UI is hidden and the error is surfaced to the user.
 */
class InterstitialFragment : Fragment() {

    private lateinit var tblClassicPage: TBLClassicPage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_classic_interstitial, container, false)
    }

    /**
     * Initializes the Taboola Classic page, sets up callbacks, and loads the interstitial.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val interstitialLoadingContainer = view.findViewById<LinearLayout>(R.id.interstitial_loading_container)
        val showInterstitialButton = view.findViewById<Button>(R.id.show_interstitial_btn)

        val properties = PlacementInfo.interstitialProperties()
        tblClassicPage = Taboola.getClassicPage(properties.pageUrl, properties.pageType)

        val listener = object : TBLClassicInterstitialListener() {
            override fun onInterstitialLoaded() {
                super.onInterstitialLoaded()
                interstitialLoadingContainer.isVisible = false
                showInterstitialButton.isVisible = true
                Log.d(TAG, "The Interstitial is loaded successfully.")
            }

            override fun onInterstitialWillPresent() {
                super.onInterstitialWillPresent()
                Log.d(TAG, "The Interstitial will be presented.")
            }

            override fun onInterstitialPresented() {
                super.onInterstitialPresented()
                showInterstitialButton.isVisible = false
                Log.d(TAG, "The Interstitial is presented.")
            }

            override fun onInterstitialWillDismiss() {
                super.onInterstitialWillDismiss()
                Log.d(TAG, "The Interstitial will be dismissed.")
            }

            override fun onInterstitialDismissed() {
                super.onInterstitialDismissed()
                Log.d(TAG, "The Interstitial is dismissed.")
            }

            override fun onInterstitialClicked() {
                super.onInterstitialClicked()
                Log.d(TAG, "The Interstitial is clicked.")
            }

            override fun interstitialDidFailToLoadAdWithError(error: String) {
                super.interstitialDidFailToLoadAdWithError(error)
                interstitialLoadingContainer.isVisible = false
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                Log.d(TAG, error)
            }
        }

        tblClassicPage.initInterstitial(
            properties.placementName,
            properties.mode,
            properties.customSegment,
            listener
        )

        tblClassicPage.loadInterstitial()

        showInterstitialButton.setOnClickListener {
            tblClassicPage.showInterstitial()
        }
    }

    companion object {
        private val TAG = InterstitialFragment::class.java.simpleName
    }
}

