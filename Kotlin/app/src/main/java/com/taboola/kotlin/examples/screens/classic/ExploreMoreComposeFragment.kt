package com.taboola.kotlin.examples.screens.classic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
 * This fragment initializes a Taboola Classic Page and sets up the back button trigger
 * that conditionally displays the "Explore More" screen upon a system back button press,
 * allowing users to view more content before exiting the view.
 * <p>
 * The fragment uses the SDK's built-in back button trigger which automatically handles
 * showing Explore More only if it has been loaded and the screen is a root screen.
 */
class ExploreMoreComposeFragment : Fragment() {

    private lateinit var tblClassicPage: TBLClassicPage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val properties = PlacementInfo.exploreMoreProperties()
        initializeTaboolaPage(properties)
        setUpBackButtonTrigger()

        return ComposeView(requireContext()).apply {
            setContent {
                ExploreMoreScreen(
                    context = requireContext(),
                    tblClassicPage = tblClassicPage
                )
            }
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
     * Sets up the back button trigger using the SDK's built-in method.
     * This will automatically show Explore More on back button press if:
     * - Explore More has been loaded successfully
     * - The screen is a root screen (back button would exit the app)
     * Note that Explore More can also be manually triggered by any other action.
     */
    private fun setUpBackButtonTrigger() {
        tblClassicPage.setExploreMoreBackButtonTrigger(
            requireActivity(),
            requireActivity().onBackPressedDispatcher,
            viewLifecycleOwner,
            requireActivity().supportFragmentManager
        )
    }

    companion object {
        val TAG: String = ExploreMoreComposeFragment::class.java.simpleName
    }
}

@Composable
fun ExploreMoreScreen(
    context: Context,
    tblClassicPage: TBLClassicPage
) {
    val properties = PlacementInfo.exploreMoreProperties()

    LaunchedEffect(Unit) {
        initExploreMore(
            tblClassicPage = tblClassicPage,
            context = context,
            properties = properties
        )
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
 *
 * @param tblClassicPage The Taboola Classic Page
 * @param context The application or activity context required for Taboola initialization
 * @param properties The placement properties for Explore More configuration
 */
private fun initExploreMore(
    tblClassicPage: TBLClassicPage,
    context: Context,
    properties: PlacementInfo.ExploreMoreProperties
) {
    val tblClassicListener = object : TBLExploreMoreClassicListener() {
        override fun onAdReceiveSuccess() {
            super.onAdReceiveSuccess()
            Log.d(ExploreMoreComposeFragment.TAG, "Taboola | onAdReceiveSuccess")
        }

        override fun exploreMoreDidOpen() {
            super.exploreMoreDidOpen()
            Log.d(ExploreMoreComposeFragment.TAG, "Taboola | exploreMoreDidOpen")
        }

        override fun onAdReceiveFail(error: String?) {
            super.onAdReceiveFail(error)
            Log.d(ExploreMoreComposeFragment.TAG, "Taboola | onAdReceiveFail: ${error ?: "Unknown error occurred during ad load."}")
        }
    }

    tblClassicPage.initExploreMore(
        context,
        tblClassicListener,
        properties.placementName,
        properties.mode,
        properties.customSegment
    )
}

