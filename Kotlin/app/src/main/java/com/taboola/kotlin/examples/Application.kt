package com.taboola.kotlin.examples

import android.app.Application
import com.taboola.android.TBLPublisherInfo
import com.taboola.android.Taboola

class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Define a publisher info object
        val publisherInfo = TBLPublisherInfo(PublisherInfo.PUBLISHER).setApiKey(PublisherInfo.API_KEY)

        // Initialize Taboola SDK as early as possible
        Taboola.init(publisherInfo)
    }
}