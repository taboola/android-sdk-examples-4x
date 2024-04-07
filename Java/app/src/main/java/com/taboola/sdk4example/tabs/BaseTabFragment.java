package com.taboola.sdk4example.tabs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.taboola.sdk4example.R;

import java.util.Locale;

public abstract class BaseTabFragment<T extends BaseTaboolaFragment> extends Fragment implements TabsContract.TabsView {

    protected TabsContract.TabsPresenter tabsPresenter = new TabsPresenterImp();
    private FragmentsAdapter<T> fragmentsAdapter;
    private ViewPager viewPager;
    private ViewPager.SimpleOnPageChangeListener simpleOnPageChangeListener;
    private TextView textView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (fragmentsAdapter == null) {
            fragmentsAdapter = new FragmentsAdapter<>(getChildFragmentManager());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);
        viewPager = view.findViewById(R.id.tabs_viewpager);
        textView = view.findViewById(android.R.id.title);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPagerAdapter(fragmentsAdapter);
        viewPager.setAdapter(fragmentsAdapter);
        viewPager.setOffscreenPageLimit(fragmentsAdapter.getCount());

        simpleOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                int currentItem = viewPager.getCurrentItem();
                tabsPresenter.setCurrentPage(currentItem);
                T fragment = fragmentsAdapter.getItem(currentItem);
                fragment.onPageSelected();
                textView.setText(getTextForItem(currentItem));

            }
        };

        viewPager.addOnPageChangeListener(simpleOnPageChangeListener);
        viewPager.post(() -> simpleOnPageChangeListener.onPageSelected(viewPager.getCurrentItem()));
    }

    @NonNull
    protected String getTextForItem(int currentItem) {
        return String.format(Locale.getDefault(), "page %d", currentItem + 1);
    }

    protected void setupViewPagerAdapter(FragmentsAdapter<T> adapter) {
        adapter.clear();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tabsPresenter.takeView(this);
        tabsPresenter.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        tabsPresenter.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        tabsPresenter.takeView(this);
        tabsPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        tabsPresenter.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tabsPresenter.onDestroyView();
    }

    @Override
    public void setCurrentPage(int currentPage) {
        viewPager.setCurrentItem(currentPage, false);
    }

}
