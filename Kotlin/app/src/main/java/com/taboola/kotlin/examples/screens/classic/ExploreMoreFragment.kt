package com.taboola.kotlin.examples.screens.classic

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.taboola.android.TBLClassicPage
import com.taboola.android.Taboola
import com.taboola.android.listeners.TBLExploreMoreClassicListener
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R

/**
 * A fragment demonstrating the integration of the Taboola SDK's Explore More feature.
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
class ExploreMoreFragment : Fragment() {

    private lateinit var tblClassicPage: TBLClassicPage
    private lateinit var showExploreMoreButton: Button
    private lateinit var setBackButtonTriggerButton: Button
    private lateinit var exploreMoreLoadingContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_explore_more, container, false)

        val properties = PlacementInfo.exploreMoreProperties()
        tblClassicPage = Taboola.getClassicPage(properties.pageUrl, properties.pageType)

        exploreMoreLoadingContainer = root.findViewById(R.id.loading_container)
        setUpButtons(root)
        initExploreMore(inflater.context, properties)

        return root
    }

    /**
     * Configures the Explore More feature by initializing it with the Taboola SDK.
     * <p>
     * Sets up listeners that handle the loading state UI:
     * - Hides the loading indicator when Explore More loads successfully or fails
     * - Shows the action buttons when Explore More loads successfully
     *
     * @param context The application or activity context required for Taboola initialization.
     * @param properties The placement properties for Explore More configuration
     */
    private fun initExploreMore(
        context: Context?,
        properties: PlacementInfo.ExploreMoreProperties
    ) {

        val tblExploreMoreClassicListener = object : TBLExploreMoreClassicListener() {
            override fun onAdReceiveSuccess() {
                super.onAdReceiveSuccess()
                Log.d(TAG, "Taboola | onAdReceiveSuccess")
                // Hide loading indicator
                exploreMoreLoadingContainer.visibility = View.GONE
                // Show buttons after Explore More is successfully loaded
                showExploreMoreButton.visibility = View.VISIBLE
                setBackButtonTriggerButton.visibility = View.VISIBLE
            }

            override fun exploreMoreDidOpen() {
                super.exploreMoreDidOpen()
                Log.d(TAG, "Taboola | exploreMoreDidOpen")
            }

            override fun onAdReceiveFail(error: String?) {
                super.onAdReceiveFail(error)
                Log.d(
                    TAG,
                    "Taboola | onAdReceiveFail: ${error ?: "Unknown error occurred during ad load."}"
                )
                // Hide loading indicator
                exploreMoreLoadingContainer.visibility = View.GONE
            }
        }

        tblClassicPage.initExploreMore(
            context,
            tblExploreMoreClassicListener,
            properties.placementName,
            properties.mode,
            properties.customSegment
        )
    }

    /**
     * Sets up the UI buttons to demonstrate both {@code showExploreMore} and
     * {@code setExploreMoreBackButtonTrigger} APIs.
     * <p>
     * The buttons are hidden initially and will appear only after Explore More is
     * successfully loaded
     * <p>
     * When the "Show Explore More" button is clicked, it immediately displays the
     * Explore More modal and both buttons disappear.
     * <p>
     * When the "Set Back Button Trigger" button is clicked, it configures the back
     * button to automatically show Explore More (if conditions are met) and both
     * buttons disappear.
     *
     * @param root The root view of the fragment
     */
    private fun setUpButtons(root: View) {
        showExploreMoreButton = root.findViewById(R.id.show_explore_more_btn)
        setBackButtonTriggerButton = root.findViewById(R.id.set_back_button_trigger_btn)

        // Hide buttons initially - they will appear after Explore More is loaded
        showExploreMoreButton.visibility = View.GONE
        setBackButtonTriggerButton.visibility = View.GONE

        showExploreMoreButton.setOnClickListener {
            Log.d(TAG, "Show Explore More button pressed")
            tblClassicPage.showExploreMore(requireActivity().supportFragmentManager)
            showExploreMoreButton.visibility = View.GONE
            setBackButtonTriggerButton.visibility = View.GONE
        }

        setBackButtonTriggerButton.setOnClickListener {
            Log.d(TAG, "Set Back Button Trigger button pressed")
            setUpBackButtonTrigger()
            showExploreMoreButton.visibility = View.GONE
            setBackButtonTriggerButton.visibility = View.GONE
        }
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
    private fun setUpBackButtonTrigger() {
        val activity = requireActivity()

        tblClassicPage.setExploreMoreBackButtonTrigger(
            activity,
            activity.onBackPressedDispatcher,
            viewLifecycleOwner,
            activity.supportFragmentManager
        )
    }

    companion object {
        val TAG: String = ExploreMoreFragment::class.java.simpleName
    }
}