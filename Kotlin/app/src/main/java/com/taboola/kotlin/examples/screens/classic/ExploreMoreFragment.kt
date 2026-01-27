package com.taboola.kotlin.examples.screens.classic

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.taboola.android.TBLClassicPage
import com.taboola.android.Taboola
import com.taboola.android.listeners.TBLExploreMoreClassicListener
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R

/**
 * A fragment demonstrating the integration of the Taboola SDK's Explore More feature.
 * <p>
 * This fragment initializes a Taboola Classic Page and sets up the back button trigger
 * that conditionally displays the "Explore More" screen upon a system back button press,
 * allowing users to view more content before exiting the view.
 * <p>
 * The fragment uses the SDK's built-in back button trigger which automatically handles
 * showing Explore More only if it has been loaded and the screen is a root screen.
 */
class ExploreMoreFragment : Fragment() {

    private lateinit var tblClassicPage: TBLClassicPage

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
        setUpBackButtonTrigger()

        return root
    }

    /**
     * Configures the Explore More feature.
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
            }

            override fun exploreMoreDidOpen() {
                super.exploreMoreDidOpen()
                Log.d(TAG, "Taboola | exploreMoreDidOpen")
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
        val TAG: String = ExploreMoreFragment::class.java.simpleName
    }
}