package com.taboola.sdk4example.sdk_web;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taboola.sdk4example.tabs.BaseTabFragment;
import com.taboola.sdk4example.tabs.BaseTaboolaFragment;
import com.taboola.sdk4example.tabs.FragmentsAdapter;
import com.taboola.android.Taboola;
import com.taboola.android.listeners.TBLWebListener;
import com.taboola.android.tblweb.TBLWebPage;
import com.taboola.android.tblweb.TBLWebUnit;
import com.taboola.android.utils.TBLAssetUtil;


public class ViewPagerViaJsFragment extends BaseTabFragment<ViewPagerViaJsFragment.SampleJsTaboolaFragment> {

    @Override
    protected void setupViewPagerAdapter(FragmentsAdapter<SampleJsTaboolaFragment> adapter) {
        super.setupViewPagerAdapter(adapter);
        adapter.addFragment(new SampleJsTaboolaFragment());
        adapter.addFragment(new SampleJsTaboolaFragment());
    }

    public static class SampleJsTaboolaFragment extends BaseTaboolaFragment {

        private static final String TAG = "SampleJsTaboolaFragment";
        private static final String HTML_CONTENT_FILE = "sampleContentPagePrefetch.html";
        private static final String BASE_URL = "https://example.com";

        private WebView webView;

        @Override
        public void onPageSelected() {
            initWebViewSettings(webView);
            loadHtml();
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final Context context = inflater.getContext();
            webView = new WebView(context);
            TBLWebPage tblWebPage = Taboola.getWebPage();
            TBLWebUnit tblWebUnit = tblWebPage.build(webView, new TBLWebListener() {
                @Override
                public boolean onItemClick(String placementName, String itemId, String clickUrl, boolean isOrganic, @Nullable String customData) {
                    return super.onItemClick(placementName, itemId, clickUrl, isOrganic, customData);
                }
            });

            initWebViewSettings(webView);
            loadHtml();
            return webView;
        }

        private void loadHtml() {

            //publisher should load its url here instead
            String htmlContent = null;
            try {
                htmlContent = TBLAssetUtil.getHtmlTemplateFileContent(getContext(), HTML_CONTENT_FILE);
            } catch (Exception e) {
                Log.e(TAG, "Failed to read asset file: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
            webView.loadDataWithBaseURL(BASE_URL, htmlContent, "text/html", "UTF-8", "");
        }


        @Override
        public void onDetach() {
            initWebViewSettings(webView);
            loadHtml();
            super.onDetach();
        }

        private static void initWebViewSettings(WebView webView) {
            final WebSettings settings = webView.getSettings();
            settings.setLoadsImagesAutomatically(true);
            settings.setLoadWithOverviewMode(true);
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setAppCacheEnabled(true);
            settings.setUseWideViewPort(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }


            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            CookieManager.getInstance().setAcceptCookie(true);
        }

    }


}
