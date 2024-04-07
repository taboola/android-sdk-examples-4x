package com.taboola.sdk4example.tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public abstract class BaseTaboolaFragment extends Fragment {

    public static final String VIEW_ID = "VIEW_ID";

    protected String getViewId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            getViewId = getArguments().getString(VIEW_ID);
        }
    }

    public void onPageSelected() {

    }

}
