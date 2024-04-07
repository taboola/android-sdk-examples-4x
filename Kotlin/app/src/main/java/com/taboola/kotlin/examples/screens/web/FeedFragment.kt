package com.taboola.kotlin.examples.screens.web

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.taboola.android.Taboola
import com.taboola.android.listeners.TBLWebListener
import com.taboola.android.tblweb.TBLWebUnit
import com.taboola.kotlin.examples.PlacementInfo
import com.taboola.kotlin.examples.R

class FeedFragment : Fragment() {
    private val BASE_URL = "https://example.com"
    private val HTML_CONTENT_FILE_TITLE = "sampleContentPage.html"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_web, container, false)
        val webView: WebView = root.findViewById(R.id.customer_webview)

        // Create a Taboola control Unit as a wrapper around your WebView
        // You can use the webUnit to refresh content, set custom flags, etc..
        val webUnit = setupWebViewForTaboola(webView)

        // Load your content
        // Note: your web content code should have Taboola tags inside
        loadWebViewContent(webView, PlacementInfo.webFeedProperties())

        return root
    }

    /**
     * Build a Taboola Page and Unit to describe the screen and placement you wish Taboola to work with.
     * Notice: Taboola requires JavaScript to be enabled in the WebView to work.
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebViewForTaboola(webView: WebView) : TBLWebUnit {
        //If not already set in your WebView
        webView.settings.javaScriptEnabled = true

        return Taboola.getWebPage().build(webView, object: TBLWebListener(){
            override fun onItemClick(placementName: String?, itemId: String?, clickUrl: String?, isOrganic: Boolean, customData: String?): Boolean {
                println("Taboola | onItemClick | isOrganic = $isOrganic")
                return super.onItemClick(placementName, itemId, clickUrl, isOrganic, customData)
            }
        })
    }

    /**
     * This method is just a quick way to load an example page simulating customer layout.
     * Inside the asset file there are Javascript tags Taboola targets with its content.
     */
    private fun loadWebViewContent(webView: WebView, properties: PlacementInfo.WebFeedProperties) {
        var htmlContent: String? = null
        try {
            htmlContent = AssetUtil.getHtmlTemplateFileContent(context, HTML_CONTENT_FILE_TITLE)
            htmlContent = htmlContent.replace("<PLACEMENT>", properties.placementName)
            htmlContent = htmlContent.replace("<MODE>", properties.mode)
            webView.loadDataWithBaseURL(BASE_URL, htmlContent, "text/html", "UTF-8", "")
        } catch (e: Exception) {
            println("Failed to read asset file: ${e.localizedMessage}")
            e.printStackTrace()
        }
    }
}