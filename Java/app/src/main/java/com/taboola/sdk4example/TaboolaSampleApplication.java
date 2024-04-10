package com.taboola.sdk4example;

import android.app.Application;

import com.taboola.android.TBLPublisherInfo;
import com.taboola.android.Taboola;

public class TaboolaSampleApplication extends Application {

    public TBLPublisherInfo tblPublisherInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        tblPublisherInfo = new TBLPublisherInfo(Const.PUBLISHER_NAME).setApiKey(Const.API_KEY);
        Taboola.init(tblPublisherInfo);
    }
}