package com.taboola.kotlin.examples.screens.classic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import com.taboola.android.TBLClassicPage
import com.taboola.android.TBLClassicUnit
import com.taboola.android.Taboola
import com.taboola.android.annotations.TBL_PLACEMENT_TYPE
import com.taboola.android.listeners.TBLClassicListener
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R

class ClassicComposeFeed : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = ComposeView(requireContext()).apply {
        setContent {
            // Create and return a Taboola Unit
            val tblClassicUnit = getTaboolaUnit(PlacementInfo.classicFeedProperties())
            ListScopeSample(tblClassicUnit = tblClassicUnit)
            tblClassicUnit.fetchContent()
        }
    }

    /**
     * Define a Page that represents this screen, get a Unit from it, add it to screen and fetch its content
     * Notice: A Unit of limited items, called "Widget" in Taboola, can be set in TBL_PLACEMENT_TYPE.PAGE_BOTTOM or TBL_PLACEMENT_TYPE.PAGE_MIDDLE
     */
    private fun getTaboolaUnit(properties: PlacementInfo.ClassicFeedProperties): TBLClassicUnit {
        // Define a page to control all Unit placements on this screen
        val classicPage: TBLClassicPage =
            Taboola.getClassicPage(properties.pageUrl, properties.pageType)

        // Define a single Unit to display
        val classicUnit: TBLClassicUnit = classicPage.build(
            context,
            properties.placementName,
            properties.mode,
            TBL_PLACEMENT_TYPE.PAGE_BOTTOM,
            object : TBLClassicListener() {
                override fun onAdReceiveSuccess() {
                    super.onAdReceiveSuccess()
                    Log.d(TAG, "Taboola | onAdReceiveSuccess")
                }

                override fun onAdReceiveFail(error: String?) {
                    super.onAdReceiveFail(error)
                    Log.d(TAG, "Taboola | onAdReceiveFail: $error")
                }
            })
        return classicUnit
    }

    companion object {
        val TAG: String = ClassicComposeFeed::class.java.simpleName
    }
}


@Composable
fun ListScopeSample(tblClassicUnit: TBLClassicUnit) {
    LazyColumn {

        // Add item
        item() {
            Text(text = stringResource(id = R.string.lorem_ipsum_long))
        }

        // Add another single item (Taboola Feed)
        item() {
            classicIntegration(tblClassicUnit = tblClassicUnit)
        }
    }
}