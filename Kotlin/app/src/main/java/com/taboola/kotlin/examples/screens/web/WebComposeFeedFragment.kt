package com.taboola.kotlin.examples.screens.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import com.taboola.kotlin.examples.PlacementInfo

/**
 * This is an example for 1 webView and 1 page, if you have another case, then please contact Taboola support team.
 */
class WebComposeFeedFragment : Fragment() {
    //Create taboolaWebWrapperViewModel instance
    //Inside the WebView apply scope we will add Taboola code to use web integration
    private val taboolaWebWrapperViewModel = TaboolaWebWrapperViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            // Add Taboola web feed to your current WebView
            CustomerWebView(PlacementInfo.webFeedProperties(), taboolaWebWrapperViewModel)
        }
    }
}

/**
 * This method adds WebView using Compose and integrate with Taboola SDK (Web Integration)
 */
@Composable
fun CustomerWebView(
    placementInfo: PlacementInfo.WebFeedProperties,
    taboolaWebWrapperViewModel: TaboolaWebWrapperViewModel
) {
    //This is the publisher code which displays a WebView on the screen
    AndroidView(factory = { context ->
        WebView(context).apply outer@{
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            taboolaWebWrapperViewModel.apply {
                setupWebViewForTaboola(this@outer)
                loadWebViewContentFeed(this@outer, placementInfo, context)
            }
        }
    })
}


