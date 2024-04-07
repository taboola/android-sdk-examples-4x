package com.taboola.kotlin.examples.screens.classic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.taboola.android.TBLClassicPage
import com.taboola.android.TBLClassicUnit
import com.taboola.android.Taboola
import com.taboola.android.annotations.TBL_PLACEMENT_TYPE
import com.taboola.android.listeners.TBLClassicListener
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R

class ProgrammaticWidgetFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_classic_programatic, container, false)
        val contentLayout = root.findViewById<LinearLayout>(R.id.content_layout)

        // Create and return a Taboola Unit
        val classicUnit = getTaboolaUnit(PlacementInfo.widgetProperties())

        // Add Unit to layout
        contentLayout.addView(classicUnit)

        // Fetch content for Unit
        classicUnit.fetchContent()

        return root
    }

    /**
     * Define a Page that represents this screen, get a Unit from it, add it to screen and fetch its content
     * Notice: A Unit of limited items, called "Widget" in Taboola, can be set in TBL_PLACEMENT_TYPE.PAGE_BOTTOM or TBL_PLACEMENT_TYPE.PAGE_MIDDLE
     */
    private fun getTaboolaUnit(properties: PlacementInfo.WidgetProperties) : TBLClassicUnit {
        // Define a page to control all Unit placements on this screen
        val classicPage: TBLClassicPage = Taboola.getClassicPage(properties.pageUrl, properties.pageType)

        // Define a single Unit to display
        val classicUnit: TBLClassicUnit = classicPage.build(context, properties.placementName, properties.mode, TBL_PLACEMENT_TYPE.PAGE_BOTTOM, object: TBLClassicListener(){
            override fun onAdReceiveSuccess() {
                super.onAdReceiveSuccess()
                println("Taboola | onAdReceiveSuccess")
            }

            override fun onAdReceiveFail(error: String?) {
                super.onAdReceiveFail(error)
                println("Taboola | onAdReceiveFail: $error")
            }
        })

        return classicUnit
    }
}


