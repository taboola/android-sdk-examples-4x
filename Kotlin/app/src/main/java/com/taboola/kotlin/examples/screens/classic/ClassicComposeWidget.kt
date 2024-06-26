package com.taboola.kotlin.examples.screens.classic

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import com.taboola.android.TBLClassicUnit
import com.taboola.android.Taboola
import com.taboola.android.annotations.TBL_PLACEMENT_TYPE
import com.taboola.android.listeners.TBLClassicListener
import com.taboola.kotlin.examples.PlacementInfo

class ClassicComposeWidget : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = ComposeView(requireContext()).apply {
        val tblClassicUnit: TBLClassicUnit = createTaboolaWidget(context)

        // Fetch content for Unit
        tblClassicUnit.fetchContent()

        setContent {
            //Add TBLClassicUnit to the UI (to layout)
            classicIntegration(tblClassicUnit = tblClassicUnit)
        }
    }

    /**
     * Define a Page that represents this screen, get a Unit from it, add it to screen and fetch its content
     * Notice: A Unit of unlimited items, called "Feed" in Taboola, can be set in TBL_PLACEMENT_TYPE.PAGE_BOTTOM only.
     */
    fun createTaboolaWidget(context: Context?): TBLClassicUnit {

        //Choose widget properties (placementName, Mode, pageUrl..etc)
        val properties: PlacementInfo.WidgetProperties = PlacementInfo.widgetProperties()

        //Create TBLClassicPage
        val tblClassicPage = Taboola.getClassicPage(properties.pageUrl, properties.pageType)

        //Define a single Unit to display
        val tblClassicUnit: TBLClassicUnit = tblClassicPage.build(
            context,
            properties.placementName,
            properties.mode,
            TBL_PLACEMENT_TYPE.PAGE_BOTTOM,
            object : TBLClassicListener() {
                override fun onAdReceiveSuccess() {
                    super.onAdReceiveSuccess()
                    Log.d(TAG,"Taboola | onAdReceiveSuccess")
                }

                override fun onAdReceiveFail(error: String?) {
                    super.onAdReceiveFail(error)
                    Log.d(TAG,"Taboola | onAdReceiveFail: $error")
                }
            })
        return tblClassicUnit
    }

    companion object {
        val TAG: String = ClassicComposeWidget::class.java.simpleName
    }
}

/**
 * Add TBLClassicUnit( which is View) to the UI (to the layout),
 * This method will be used in each case in the app when we need to Add TBLClassicUnit (Widget/Feed)
 */
@Composable
fun classicIntegration(tblClassicUnit: TBLClassicUnit) {
    AndroidView(factory = {
        tblClassicUnit
    })
}