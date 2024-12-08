package com.taboola.kotlin.examples

class PlacementInfo {

    open class BaseProperties {
        open val placementName = "Widget without video"
        open val sourceType = "text"
        open val pageType = "article"
        open val pageUrl = "https://blog.taboola.com"
        open val targetType = "mix"
        open val mode = "thumbs-feed-01"
    }

    class WidgetProperties : BaseProperties()

    class ClassicFeedProperties : BaseProperties() {
        override val placementName = "Feed with video"
    }

    class NativeFeedProperties : BaseProperties() {
        override val placementName = "list_item"
    }

    class WebFeedProperties : BaseProperties() {
        override val placementName = "Feed without video"
    }

    class MetaWidgetProperties : BaseProperties() {
        override val placementName = "Below Article Thumbnails"
        override val mode = "meta-widget-1x1"
    }

    class MetaFeedProperties : BaseProperties() {
        override val placementName = "Feed without video"
        override val mode = "alternating-thumbnails-a"
    }

    // Static access
    companion object {
        fun widgetProperties() = WidgetProperties()
        fun classicFeedProperties() = ClassicFeedProperties()
        fun nativeFeedProperties() = NativeFeedProperties()
        fun webFeedProperties() = WebFeedProperties()
        fun metaWidgetProperties() = MetaWidgetProperties()
        fun metaFeedProperties() = MetaFeedProperties()
    }
}