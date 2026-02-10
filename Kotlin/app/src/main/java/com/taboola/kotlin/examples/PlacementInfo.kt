package com.taboola.kotlin.examples

class PlacementInfo {

    class WidgetProperties {
        val placementName = "Widget without video"
        val sourceType = "text"
        val pageType = "article"
        val pageUrl = "https://blog.taboola.com"
        val targetType = "mix"
        val mode = "thumbs-feed-01"
    }

    class ClassicFeedProperties {
        val placementName = "Feed with video"
        val sourceType = "text"
        val pageType = "article"
        val pageUrl = "https://blog.taboola.com"
        val targetType = "mix"
        val mode = "thumbs-feed-01"
    }

    class NativeFeedProperties {
        val placementName = "list_item"
        val sourceType = "text"
        val pageType = "article"
        val pageUrl = "https://blog.taboola.com"
        val targetType = "mix"
        val mode = "thumbs-feed-01"
    }

    class WebFeedProperties {
        val placementName = "Feed without video"
        val sourceType = "text"
        val pageType = "article"
        val pageUrl = "https://blog.taboola.com"
        val targetType = "mix"
        val mode = "thumbs-feed-01"
    }

    class ExploreMoreProperties {
        val placementName = "Feed - Explore More"
        val pageType = "article"
        val pageUrl = "https://blog.taboola.com"
        val mode = "thumbs-feed-01"
        val customSegment = "subscriber"
    }

    class InterstitialProperties {
        val placementName = "Trigger Interstitial"
        val pageType = "article"
        val pageUrl = "https://blog.taboola.com"
        val mode = "full-screen-interstitial-test"
        val customSegment = "Vignette"
    }

    // Static access
    companion object {
        fun widgetProperties() = WidgetProperties()
        fun classicFeedProperties() = ClassicFeedProperties()

        fun nativeFeedProperties() = NativeFeedProperties()
        fun webFeedProperties() = WebFeedProperties()
        fun exploreMoreProperties() = ExploreMoreProperties()
        fun interstitialProperties() = InterstitialProperties()
    }


}