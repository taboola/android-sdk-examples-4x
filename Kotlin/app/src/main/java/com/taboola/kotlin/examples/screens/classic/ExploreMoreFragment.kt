package com.taboola.kotlin.examples.screens.classic

import android.content.Context
import android.os.Bundle
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

        buildExploreMore(inflater.context)
        setUpBackPressedHandler()

        return root
    }

    private fun buildExploreMore(context: Context?) {
        val properties = PlacementInfo.exploreMoreProperties()

        tblClassicPage = Taboola.getClassicPage(properties.pageUrl, properties.pageType)

        val tblExploreMoreClassicListener = object : TBLExploreMoreClassicListener() {
            override fun onAdReceiveSuccess() {
                super.onAdReceiveSuccess()
                shouldShowExploreMore = true
            }

            override fun exploreMoreDidOpen() {
                super.exploreMoreDidOpen()
                shouldShowExploreMore = false
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
}