package com.taboola.kotlin.examples.screens.classic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.fragment.app.Fragment
import com.taboola.android.TBLClassicPage
import com.taboola.android.Taboola
import com.taboola.android.listeners.TBLExploreMoreClassicListener
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R

/**
 * A fragment demonstrating the integration of the Taboola SDK's Explore More feature using Jetpack Compose.
 * <p>
 * This fragment initializes a Taboola Classic Page and provides UI to demonstrate
 * both the {@code showExploreMore} and {@code setExploreMoreBackButtonTrigger} APIs.
 * <p>
 * The fragment displays a loading indicator ("Explore More Loading") while Explore More
 * is being loaded. Once loading completes successfully, two buttons appear:
 * <ul>
 *   <li>"Show Explore More" - Manually triggers the Explore More modal</li>
 *   <li>"Set Back Button Trigger" - Sets up automatic Explore More display on back button press</li>
 * </ul>
 * Both buttons disappear after either one is clicked to indicate that an action has been taken.
 * <p>
 * The back button trigger will automatically show Explore More on back button press if:
 * - Explore More has been loaded successfully
 * - The screen is a root screen (back button would exit the app)
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
        initializeTaboolaPage(exploreMoreProperties = PlacementInfo.exploreMoreProperties())

        return ComposeView(requireContext()).apply {
            setContent {
                ExploreMoreScreen(
                    context = requireContext(),
                    tblClassicPage = tblClassicPage,
                    fragment = this@ExploreMoreComposeFragment
                )
            }
        }
    }

    /**
     * Define a Page that represents this screen
     */
    private fun initializeTaboolaPage(
        exploreMoreProperties: PlacementInfo.ExploreMoreProperties
    ) {
        val classicPage: TBLClassicPage =
            Taboola.getClassicPage(exploreMoreProperties.pageUrl, exploreMoreProperties.pageType)

        tblClassicPage = classicPage
    }

    /**
     * Sets up the back button trigger using the SDK's built-in method.
     * <p>
     * This configures the system back button to automatically show Explore More
     * when pressed, but only if:
     * <ul>
     *   <li>Explore More has been loaded successfully</li>
     *   <li>The screen is a root screen (back button would exit the app)</li>
     * </ul>
     * This method is called when the user clicks the "Set Back Button Trigger" button.
     */
    fun setUpBackButtonTrigger() {
        val activity = requireActivity()

        tblClassicPage.setExploreMoreBackButtonTrigger(
            activity,
            activity.onBackPressedDispatcher,
            viewLifecycleOwner,
            activity.supportFragmentManager
        )
    }

    companion object {
        val TAG: String = ExploreMoreComposeFragment::class.java.simpleName
    }
}

@Composable
fun ExploreMoreScreen(
    context: Context,
    tblClassicPage: TBLClassicPage,
    fragment: ExploreMoreComposeFragment
) {
    var isLoadingVisible by remember { mutableStateOf(true) }

    // Controls whether the action buttons ("Show Explore More" and "Set Back Button Trigger") should be displayed.
    // Set to true when Explore More loads successfully, and false after either button is clicked.
    var shouldShowActionButtons by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        initExploreMore(
            tblClassicPage = tblClassicPage,
            context = context,
            properties = PlacementInfo.exploreMoreProperties(),
            onLoadingStateChanged = { isLoading ->
                isLoadingVisible = isLoading
            },
            onButtonsVisibilityChanged = { areVisible ->
                shouldShowActionButtons = areVisible
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.lorem_ipsum),
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Loading indicator
        if (isLoadingVisible) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(end = 10.dp))
                Text(
                    text = stringResource(id = R.string.explore_more_loading),
                )
            }
        }

        // Show Explore More button
        if (shouldShowActionButtons) {
            Button(
                onClick = {
                    Log.d(ExploreMoreComposeFragment.TAG, "Show Explore More button pressed")
                    tblClassicPage.showExploreMore(fragment.requireActivity().supportFragmentManager)
                    shouldShowActionButtons = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = stringResource(id = R.string.show_explore_more))
            }

            // Set Back Button Trigger button
            Button(
                onClick = {
                    Log.d(ExploreMoreComposeFragment.TAG, "Set Back Button Trigger button pressed")
                    fragment.setUpBackButtonTrigger()
                    shouldShowActionButtons = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = stringResource(id = R.string.set_back_button_trigger))
            }
        }
    }
}

/**
 * Configures the Explore More feature by initializing it with the Taboola SDK.
 * <p>
 * Sets up listeners that handle the loading state UI:
 * - Hides the loading indicator when Explore More loads successfully or fails
 * - Shows the action buttons when Explore More loads successfully
 *
 * @param tblClassicPage The Taboola Classic Page
 * @param context The application or activity context required for Taboola initialization
 * @param properties The placement properties for Explore More configuration
 * @param onLoadingStateChanged Callback to update loading indicator visibility
 * @param onButtonsVisibilityChanged Callback to update buttons visibility
 */
private fun initExploreMore(
    tblClassicPage: TBLClassicPage,
    context: Context,
    properties: PlacementInfo.ExploreMoreProperties,
    onLoadingStateChanged: (Boolean) -> Unit,
    onButtonsVisibilityChanged: (Boolean) -> Unit
) {
    val tblClassicListener = object : TBLExploreMoreClassicListener() {
        override fun onAdReceiveSuccess() {
            super.onAdReceiveSuccess()
            Log.d(ExploreMoreComposeFragment.TAG, "Taboola | onAdReceiveSuccess")
            // Hide loading indicator
            onLoadingStateChanged(false)
            // Show buttons after Explore More is successfully loaded
            onButtonsVisibilityChanged(true)
        }

        override fun exploreMoreDidOpen() {
            super.exploreMoreDidOpen()
            Log.d(ExploreMoreComposeFragment.TAG, "Taboola | exploreMoreDidOpen")
        }

        override fun onAdReceiveFail(error: String?) {
            super.onAdReceiveFail(error)
            Log.d(
                ExploreMoreComposeFragment.TAG,
                "Taboola | onAdReceiveFail: ${error ?: "Unknown error occurred during ad load."}"
            )
            // Hide loading indicator
            onLoadingStateChanged(false)
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

