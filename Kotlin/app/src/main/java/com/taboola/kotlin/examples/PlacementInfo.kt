package com.taboola.kotlin.examples

class PlacementInfo {

    class WidgetProperties  {
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

    class WebFeedProperties  {
        val placementName = "Feed without video"
        val sourceType = "text"
        val pageType = "article"
        val pageUrl = "https://blog.taboola.com"
        val targetType = "mix"
        val mode = "thumbs-feed-01"
    }

    class MetaWidgetProperties  {
        val placementName = "Below Article Thumbnails"
        val sourceType = "text"
        val pageType = "article"
        val pageUrl = "https://blog.taboola.com"
        val targetType = "mix"
        val mode = "meta-widget-1x1"
    }

    class MetaFeedProperties  {
        val placementName = "Feed without video"
        val sourceType = "text"
        val pageType = "article"
        val pageUrl = "https://blog.taboola.com"
        val targetType = "mix"
        val mode = "alternating-thumbnails-a"
    }

    // Static access
    companion object  {
        fun widgetProperties() = WidgetProperties()
        fun classicFeedProperties() = ClassicFeedProperties()
        fun nativeFeedProperties() = NativeFeedProperties()
        fun webFeedProperties() = WebFeedProperties()
        fun metaWidgetProperties() = MetaWidgetProperties()
        fun metaFeedProperties() = MetaFeedProperties()
    }
}