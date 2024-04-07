package com.taboola.kotlin.examples.screens.classic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.taboola.android.TBLClassicUnit
import com.taboola.android.Taboola
import com.taboola.android.annotations.TBL_PLACEMENT_TYPE
import com.taboola.android.listeners.TBLClassicListener
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R

class XmlWidgetFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_classic_xml, container, false)

        // Find the Taboola Unit in XML layout
        val classicUnit = root.findViewById<TBLClassicUnit>(R.id.classic_unit)
        val properties = PlacementInfo.widgetProperties()

        // Create a Taboola Page that represents this screen
        val taboolaPage = Taboola.getClassicPage(properties.pageUrl, properties.pageType)

        // Add the Unit to the Taboola Page
        taboolaPage.addUnitToPage(classicUnit, properties.placementName, properties.mode, TBL_PLACEMENT_TYPE.PAGE_BOTTOM, object: TBLClassicListener(){
            override fun onAdReceiveSuccess() {
                super.onAdReceiveSuccess()
                println("Taboola | onAdReceiveSuccess")
            }

            override fun onAdReceiveFail(error: String?) {
                super.onAdReceiveFail(error)
                println("Taboola | onAdReceiveFail: $error")
            }
        })

        // Fetch content for Unit
        classicUnit.fetchContent()

        return root
    }
}


