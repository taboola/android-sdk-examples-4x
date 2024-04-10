package com.taboola.sdk4example.tabs;

import android.os.Bundle;

public class TabsPresenterImp implements TabsContract.TabsPresenter {

    private static final String TAG = "PagePresenterImp";
    private static final String PAGE_KEY = TAG + " " + "page_key";
    private TabsContract.TabsView tabsView;
    private int currentPage;


    @Override
    public void takeView(TabsContract.TabsView view) {
        tabsView = view;
    }

    @Override
    public void dropView() {
        tabsView = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        currentPage = savedInstanceState.getInt(PAGE_KEY);
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        out.putInt(PAGE_KEY, currentPage);
    }

    @Override
    public void onStart() {
        tabsView.setCurrentPage(currentPage);
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroyView() {
        dropView();
    }


    @Override
    public void setCurrentPage(int position) {
        currentPage = position;
    }
}
