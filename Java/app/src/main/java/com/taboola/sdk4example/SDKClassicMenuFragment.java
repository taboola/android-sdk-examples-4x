package com.taboola.sdk4example;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taboola.sdk4example.sdk_classic.FeedLazyLoadInsideRecyclerViewFragment;
import com.taboola.sdk4example.sdk_classic.FeedWithMiddleArticleDarkModeInsideRecyclerViewFragment;
import com.taboola.sdk4example.sdk_classic.FeedWithMiddleArticleInsideListViewFragment;
import com.taboola.sdk4example.sdk_classic.FeedWithMiddleArticleInsideRecyclerViewFragment;
import com.taboola.sdk4example.sdk_classic.FeedWithMiddleArticleInsideScrollViewFragment;
import com.taboola.sdk4example.sdk_classic.MetaAdInsideScrollViewFragment;
import com.taboola.sdk4example.sdk_classic.MetaClassicUnitFragment;
import com.taboola.sdk4example.sdk_classic.OCClickHandlerFragment;
import com.taboola.sdk4example.sdk_classic.PullToRefreshFragment;
import com.taboola.sdk4example.sdk_classic.RecyclerViewPreloadFragment;
import com.taboola.sdk4example.sdk_classic.SimpleWidgetFragment;
import com.taboola.sdk4example.sdk_classic.ViewPagerFragment;


public class SDKClassicMenuFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener onFragmentInteractionListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup viewGroup = view.findViewById(R.id.main_menu_lyt);
        addButton(getString(R.string.std_mid_article_with_feed_lnr), R.id.std_mid_article_with_feed_lnr, viewGroup);
        addButton(getString(R.string.std_mid_article_with_feed_rv), R.id.std_mid_article_with_feed_rv, viewGroup);
        addButton(getString(R.string.std_mid_article_preload), R.id.std_widget_preload, viewGroup);
        addButton(getString(R.string.std_view_pager), R.id.std_view_pager, viewGroup);
        addButton(getString(R.string.std_mid_article_with_feed_lv), R.id.std_mid_article_with_feed_lv, viewGroup);
        addButton(getString(R.string.std_widget_xml), R.id.std_widget_xml, viewGroup);
        addButton(getString(R.string.std_widget_oc_click), R.id.std_widget_oc_click, viewGroup);
        addButton(getString(R.string.std_feed_pull_to_refresh), R.id.std_feed_pull_to_refresh, viewGroup);
        addButton(getString(R.string.std_feed_lazy_loading_rv), R.id.std_feed_lazy_loading_rv, viewGroup);
        addButton(getString(R.string.std_mid_article_with_feed_dark_mode_rv), R.id.std_mid_article_with_feed_dark_mode_rv, viewGroup);
        addButton(getString(R.string.std_meta), R.id.std_meta, viewGroup);
        addButton(getString(R.string.std_meta_classic_unit), R.id.std_meta_classic_unit, viewGroup);
    }


    @Override
    public void onClick(View v) {
        String screenName = v.getTag().toString();
        Fragment fragmentToOpen = null;
        switch (v.getId()) {
            case R.id.std_mid_article_with_feed_lnr:
                fragmentToOpen = new FeedWithMiddleArticleInsideScrollViewFragment();
                break;
            case R.id.std_view_pager:
                fragmentToOpen = new ViewPagerFragment();
                break;
            case R.id.std_mid_article_with_feed_lv:
                fragmentToOpen = new FeedWithMiddleArticleInsideListViewFragment();
                break;

            case R.id.std_mid_article_with_feed_rv:
                fragmentToOpen = new FeedWithMiddleArticleInsideRecyclerViewFragment();
                break;
            case R.id.std_widget_xml:
                fragmentToOpen = new SimpleWidgetFragment();
                break;
            case R.id.std_widget_oc_click:
                fragmentToOpen = new OCClickHandlerFragment();
                break;
            case R.id.std_widget_preload:
                fragmentToOpen = new RecyclerViewPreloadFragment();
                break;
            case R.id.std_feed_pull_to_refresh:
                fragmentToOpen = new PullToRefreshFragment();
                break;
            case R.id.std_feed_lazy_loading_rv:
                fragmentToOpen = new FeedLazyLoadInsideRecyclerViewFragment();
                break;
            case R.id.std_mid_article_with_feed_dark_mode_rv:
                fragmentToOpen = new FeedWithMiddleArticleDarkModeInsideRecyclerViewFragment();
                break;
            case R.id.std_meta:
                fragmentToOpen = new MetaAdInsideScrollViewFragment();
                break;
            case R.id.std_meta_classic_unit:
                fragmentToOpen = new MetaClassicUnitFragment();
                break;
        }

        if (fragmentToOpen != null) {
            openFragment(fragmentToOpen, screenName);
        }
    }

    private void openFragment(Fragment fragment, String screenName) {
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.onMenuItemClicked(fragment, screenName);
        }
    }

    private void addButton(String screenName, int id, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.button_item, viewGroup, false);
        textView.setText(screenName);
        textView.setTag(screenName);
        textView.setId(id);
        textView.setOnClickListener(this);

        viewGroup.addView(textView, viewGroup.getChildCount() - 1);
    }


    public interface OnFragmentInteractionListener {
        void onMenuItemClicked(Fragment fragment, String screenName);
    }

}