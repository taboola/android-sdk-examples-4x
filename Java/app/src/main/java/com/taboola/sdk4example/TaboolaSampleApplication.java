package com.taboola.sdk4example;
import android.app.Application;
import com.taboola.android.TBLPublisherInfo;
import com.taboola.android.Taboola;
public class TaboolaSampleApplication extends  Application{


   public  TBLPublisherInfo tblPublisherInfo;
    @Override
    public void onCreate() {
        super.onCreate();
        tblPublisherInfo  = new TBLPublisherInfo("sdk-tester-demo").setApiKey("30dfcf6b094361ccc367bbbef5973bdaa24dbcd6");
        Taboola.init(tblPublisherInfo);
    }

}