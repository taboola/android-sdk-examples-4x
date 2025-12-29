package com.taboola.kotlin.examples.screens.classic

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.taboola.android.TBLClassicPage
import com.taboola.android.Taboola
import com.taboola.android.listeners.TBLExploreMoreClassicListener
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R

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
class ExploreMoreFragment : Fragment() {

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
    ): View? {
        val root = inflater.inflate(R.layout.fragment_explore_more, container, false)

        val properties = PlacementInfo.exploreMoreProperties()
        tblClassicPage = Taboola.getClassicPage(properties.pageUrl, properties.pageType)

        initExploreMore(inflater.context, properties)
        setUpBackPressedHandler()

        return root
    }

    /**
     * Configures the Explore More feature.
     * <p>
     * Sets up a listener to track when the Explore More content is successfully received
     * and when the screen is opened, updating the {@code shouldShowExploreMore} flag accordingly.
     *
     * @param context The application or activity context required for Taboola initialization.
     */
    private fun initExploreMore(
        context: Context?,
        properties: PlacementInfo.ExploreMoreProperties
    ) {

        val tblExploreMoreClassicListener = object : TBLExploreMoreClassicListener() {
            override fun onAdReceiveSuccess() {
                super.onAdReceiveSuccess()
                Log.d(TAG, "Taboola | onAdReceiveSuccess")
                shouldShowExploreMore = true
            }

            override fun exploreMoreDidOpen() {
                super.exploreMoreDidOpen()
                Log.d(TAG, "Taboola | exploreMoreDidOpen")
                shouldShowExploreMore = false
            }

            override fun onAdReceiveFail(error: String?) {
                super.onAdReceiveFail(error)
                Log.d(TAG, "Taboola | onAdReceiveFail: ${error ?: "Unknown error occurred during ad load."}")
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
     * Sets up the handler to intercept the system's back button press.
     * If the condition 'shouldShowExploreMore' is true, it displays the Explore More screen instead of navigating back.
     * Note that Explore More can also be manually triggered by any other action.
     */
    private fun setUpBackPressedHandler() {
        // Create an OnBackPressedCallback object
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Check if Explore More should be displayed
                if (shouldShowExploreMore) {
                    // If true, call showExploreMore
                    tblClassicPage.showExploreMore(requireActivity().supportFragmentManager)
                } else {
                    // If false, disable this callback and manually trigger the system's back press
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        // Add the callback to the activity's back press dispatcher
        requireActivity().onBackPressedDispatcher
            .addCallback(getViewLifecycleOwner(), callback)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Check if the selected menu item is the the back arrow on the Toolbar/ActionBar and if Explore More should be revealed
        if (item.itemId == android.R.id.home && shouldShowExploreMore) {
            tblClassicPage.showExploreMore(requireActivity().supportFragmentManager)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        val TAG: String = ExploreMoreFragment::class.java.simpleName
    }
}