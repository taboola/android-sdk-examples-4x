package com.taboola.sdk4example;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import androidx.fragment.app.FragmentActivity;

public class MetaUtils {
    private static final String FONTS_PATH = "fonts/";
    private static final String FONT_SUFFIX = ".ttf";

    public static Typeface loadFont(FragmentActivity activity, String fontName) {
        AssetManager assets = activity.getAssets();
        return Typeface.createFromAsset(assets, FONTS_PATH + fontName + FONT_SUFFIX);
    }
}
