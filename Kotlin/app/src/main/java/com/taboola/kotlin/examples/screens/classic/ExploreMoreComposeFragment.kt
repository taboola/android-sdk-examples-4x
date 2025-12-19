package com.taboola.kotlin.examples.screens.classic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.taboola.android.TBLClassicPage
import com.taboola.android.Taboola
import com.taboola.android.listeners.TBLExploreMoreClassicListener
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R

/**
 * A fragment demonstrating the integration of the Taboola SDK's Explore More feature using Jetpack Compose.
 * <p>
 * This fragment initializes a Taboola Classic Page and implements a custom back press handler
 * that conditionally displays the "Explore More" screen upon a system back button press,
 * allowing users to view more content before exiting the view.
 * <p>
 * The fragment keeps track of Explore More status and it will show Explore More
 * only if it has been loaded and has not been shown
 */
class ExploreMoreComposeFragment : Fragment() {

    private lateinit var tblClassicPage: TBLClassicPage
    private var shouldShowExploreMore = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            initializeTaboolaPage(PlacementInfo.exploreMoreProperties())

            ExploreMoreScreen(
                context = requireContext(),
                setShouldShowExploreMore = ::setShouldShowExploreMore,
                showExploreMore = ::showExploreMore,
                tblClassicPage = tblClassicPage
            )
        }
    }

    /**
     * Define a Page that represents this screen
     */
    private fun initializeTaboolaPage(
        properties: PlacementInfo.ExploreMoreProperties
    ) {
        val classicPage: TBLClassicPage =
            Taboola.getClassicPage(properties.pageUrl, properties.pageType)

        tblClassicPage = classicPage
    }

    /**
     * Displays the Explore More screen using the Taboola SDK.
     */
    fun showExploreMore() {
        tblClassicPage.showExploreMore(requireActivity().supportFragmentManager)
    }

    /**
     * Updates the shouldShowExploreMore flag.
     * This is called from the composable when the Explore More state changes.
     */
    fun setShouldShowExploreMore(shouldShow: Boolean) {
        shouldShowExploreMore = shouldShow
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Check if the selected menu item is the back arrow on the Toolbar/ActionBar and if Explore More should be revealed
        if (item.itemId == android.R.id.home && shouldShowExploreMore) {
            showExploreMore()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        val TAG: String = ExploreMoreComposeFragment::class.java.simpleName
    }
}

@Composable
fun ExploreMoreScreen(
    context: Context,
    setShouldShowExploreMore: (Boolean) -> Unit,
    showExploreMore: () -> Unit,
    tblClassicPage: TBLClassicPage
) {
    val properties = remember { PlacementInfo.exploreMoreProperties() }
    var shouldShowExploreMore by remember { mutableStateOf(false) }

    remember(tblClassicPage, properties) {
        initExploreMore(
            tblClassicPage = tblClassicPage,
            context = context,
            properties = properties,
            onShouldShowExploreMoreChanged = {
                shouldShowExploreMore = it
                setShouldShowExploreMore(it)
            }
        )
    }

    // Handle back button press to show Explore More
    BackHandler(enabled = shouldShowExploreMore) {
        showExploreMore()
    }

    Text(
        text = stringResource(id = R.string.lorem_ipsum),
        fontSize = 20.sp,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    )
}

/**
 * Configures the Explore More feature.
 * <p>
 * Sets up a listener to track when the Explore More content is successfully received
 * and when the screen is opened, updating the shouldShowExploreMore flag accordingly.
 *
 * @param tblClassicPage The Taboola Classic Page
 * @param context The application or activity context required for Taboola initialization
 * @param properties The placement properties for Explore More configuration
 * @param onShouldShowExploreMoreChanged Callback to update the shouldShowExploreMore state
 */
private fun initExploreMore(
    tblClassicPage: TBLClassicPage,
    context: Context,
    properties: PlacementInfo.ExploreMoreProperties,
    onShouldShowExploreMoreChanged: (Boolean) -> Unit
) {
    val listener = object : TBLExploreMoreClassicListener() {
        override fun onAdReceiveSuccess() {
            super.onAdReceiveSuccess()
            Log.d(ExploreMoreComposeFragment.TAG, "Taboola | onAdReceiveSuccess")
            onShouldShowExploreMoreChanged(true)
        }

        override fun exploreMoreDidOpen() {
            super.exploreMoreDidOpen()
            Log.d(ExploreMoreComposeFragment.TAG, "Taboola | exploreMoreDidOpen")
            onShouldShowExploreMoreChanged(false)
        }

        override fun onAdReceiveFail(error: String?) {
            super.onAdReceiveFail(error)
            Log.d(ExploreMoreComposeFragment.TAG, "Taboola | onAdReceiveFail: $error")
        }
    }

    tblClassicPage.initExploreMore(
        context,
        listener,
        properties.placementName,
        properties.mode,
        properties.customSegment
    )
}

