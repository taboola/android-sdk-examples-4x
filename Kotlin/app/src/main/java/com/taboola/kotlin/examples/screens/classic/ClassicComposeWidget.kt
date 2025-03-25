package com.taboola.kotlin.examples.screens.classic

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.taboola.android.TBLClassicComposeUnit
import com.taboola.android.Taboola
import com.taboola.android.annotations.TBL_PLACEMENT_TYPE
import com.taboola.android.listeners.TBLClassicListener
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R

class ClassicComposeWidget : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        val tblClassicComposeUnit: TBLClassicComposeUnit = createTaboolaWidget(context)

        // Fetch content for Unit
        tblClassicComposeUnit.fetchContent()

        setContent {
            //Add TBLClassicUnit to the UI (to layout)
            classicIntegration(tblClassicComposeUnit = tblClassicComposeUnit)
        }
    }

    /**
     * Define a Page that represents this screen, get a Unit from it, add it to screen and fetch its content
     * Notice: A Unit of unlimited items, called "Feed" in Taboola, can be set in TBL_PLACEMENT_TYPE.PAGE_BOTTOM only.
     */
    fun createTaboolaWidget(context: Context?): TBLClassicComposeUnit {

        //Choose widget properties (placementName, Mode, pageUrl..etc)
        val properties: PlacementInfo.WidgetProperties = PlacementInfo.widgetProperties()

        //Create TBLClassicPage
        val tblClassicPage = Taboola.getClassicPage(properties.pageUrl, properties.pageType)

        //Define a single Unit to display
        val tblClassicUnit: TBLClassicComposeUnit = tblClassicPage.buildComposeUnit(
            context,
            properties.placementName,
            properties.mode,
            TBL_PLACEMENT_TYPE.PAGE_BOTTOM,
            object: TBLClassicListener() {
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
fun classicIntegration(tblClassicComposeUnit: TBLClassicComposeUnit) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
        Text(stringResource(id = R.string.lorem_ipsum_long))
        Spacer(modifier = Modifier.height(500.dp))
        tblClassicComposeUnit.GetClassicUnitView(state = scrollState)
    }

}