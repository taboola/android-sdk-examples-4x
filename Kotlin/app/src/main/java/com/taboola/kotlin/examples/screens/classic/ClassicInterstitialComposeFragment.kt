package com.taboola.kotlin.examples.screens.classic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.taboola.android.TBLClassicPage
import com.taboola.android.Taboola
import com.taboola.android.listeners.TBLClassicInterstitialListener
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R

/**
 * Compose sample for the Taboola Classic interstitial flow.
 *
 * The screen loads an interstitial when entering the fragment and enables the
 * "Show Interstitial" button only after a successful load.
 */
class ClassicInterstitialComposeFragment : Fragment() {

    private lateinit var tblClassicPage: TBLClassicPage
    private var isLoading by mutableStateOf(true)
    private var canShowInterstitial by mutableStateOf(false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val interstitialProperties = PlacementInfo.interstitialProperties()
        tblClassicPage = Taboola.getClassicPage(
            interstitialProperties.pageUrl,
            interstitialProperties.pageType
        )
        isLoading = true
        canShowInterstitial = false
        tblClassicPage.initInterstitial(
            interstitialProperties.placementName,
            interstitialProperties.mode,
            interstitialProperties.customSegment,
            object : TBLClassicInterstitialListener() {
                override fun onInterstitialLoaded() {
                    super.onInterstitialLoaded()
                    isLoading = false
                    canShowInterstitial = true
                    Log.d(TAG, "The Interstitial is loaded successfully.")
                }

                override fun onInterstitialWillPresent() {
                    super.onInterstitialWillPresent()
                    Log.d(TAG, "The Interstitial will be presented.")
                }

                override fun onInterstitialPresented() {
                    super.onInterstitialPresented()
                    canShowInterstitial = false
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
                    isLoading = false
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, error)
                }
            }
        )
        tblClassicPage.loadInterstitial()

        return ComposeView(requireContext()).apply {
            setContent {
                InterstitialComposeScreen(
                    tblClassicPage = tblClassicPage,
                    isLoading = isLoading,
                    canShowInterstitial = canShowInterstitial
                )
            }
        }
    }

    companion object {
        val TAG = this::class.java.simpleName
    }
}

@Composable
private fun InterstitialComposeScreen(
    tblClassicPage: TBLClassicPage,
    isLoading: Boolean,
    canShowInterstitial: Boolean
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = stringResource(id = R.string.lorem_ipsum))

        if (isLoading) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CircularProgressIndicator()
                Text(text = stringResource(id = R.string.interstitial_loading))
            }
        }

        if (canShowInterstitial) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { tblClassicPage.showInterstitial() }
            ) {
                Text(text = stringResource(id = R.string.interstitial_show))
            }
        }
    }
}