package com.taboola.sdk4example.tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public abstract class BaseTaboolaFragment extends Fragment {

    public static final String VIEW_ID = "VIEW_ID";

    protected String viewId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            viewId = getArguments().getString(VIEW_ID);
        }
    }

    public void onPageSelected() {

    }

}
