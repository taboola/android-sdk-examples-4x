package com.taboola.sdk4example.sdk_classic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.taboola.sdk4example.R;
import com.taboola.android.TBLClassicPage;
import com.taboola.android.TBLClassicUnit;
import com.taboola.android.Taboola;
import com.taboola.android.annotations.TBL_PLACEMENT_TYPE;
import com.taboola.android.listeners.TBLClassicListener;

public class SimpleWidgetFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_simple_widget, container, false);
        TBLClassicUnit tblClassicUnit =view.findViewById(R.id.taboola_view);

        TBLClassicPage tblClassicPage=Taboola.getClassicPage("https://blog.taboola.com", "article");

        TBLClassicListener tblClassicListener=new TBLClassicListener() {
            @Override
            public boolean onItemClick(String placementName, String itemId, String clickUrl, boolean isOrganic, String customData) {
                return super.onItemClick(placementName, itemId, clickUrl, isOrganic, customData);
            }
        };
        tblClassicPage.addUnitToPage(tblClassicUnit,"Below Article","alternating-widget-without-video-1x4",
                                     TBL_PLACEMENT_TYPE.PAGE_BOTTOM,tblClassicListener);


        tblClassicUnit.fetchContent();


        return view;


    }
}
