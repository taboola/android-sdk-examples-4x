package com.taboola.sdk4example.sdk_web;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.taboola.android.Taboola;
import com.taboola.android.listeners.TBLWebListener;
import com.taboola.android.tblweb.TBLWebPage;
import com.taboola.android.tblweb.TBLWebUnit;
import com.taboola.android.utils.TBLAssetUtil;
import com.taboola.sdk4example.R;

public class SDKWebContinuousScroll extends AppCompatActivity {

    private static final String TAG = "DEBUG";
    private WebView mWebView;
    private Toolbar mToolbar;
    private String HTML_CONTENT_FILE_TITLE = "sampleContinuousScrollPage.html";
    private static final String base_url = "https://example.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sdkweb_continuous_scroll);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setLogo(R.drawable.ic_taboola);
        getSupportActionBar().setTitle("SDK Continuous Scroll");
        resetToolbarTitle();
        mWebView = findViewById(R.id.webview);


        TBLWebPage tblWebPage = Taboola.getWebPage();;
        TBLWebUnit tblWebUnit = tblWebPage.build(mWebView, new TBLWebListener() {
            @Override
            public boolean onItemClick(String placementName, String itemId, String clickUrl, boolean isOrganic, @Nullable String customData) {
                return super.onItemClick(placementName, itemId, clickUrl, isOrganic, customData);
            }
        });

        initWebViewSettings(mWebView);
        loadHtml();
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

    private void resetToolbarTitle() {
        mToolbar.setTitle("SDK Web Continuous Scroll");
    }

    private void loadHtml () {
        String htmlContent = null;
        try {
            htmlContent = TBLAssetUtil.getHtmlTemplateFileContent(getApplicationContext(), HTML_CONTENT_FILE_TITLE);
        } catch (Exception e) {
            Log.e(TAG, "Failed to read asset file: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        mWebView.loadDataWithBaseURL(base_url, htmlContent, "text/html", "UTF-8", "");
    }
}