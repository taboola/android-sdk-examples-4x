package com.taboola.sdk4example.utils;

import static com.taboola.sdk4example.MetaConst.TYPEFACE_ARIAL_BOLD;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;

import androidx.fragment.app.FragmentActivity;

public class Utils {
    private static final String FONTS_PATH = "fonts/";
    private static final String FONT_SUFFIX = ".ttf";

    public static Typeface loadFont(FragmentActivity activity, String fontName) {
        Typeface font = Typeface.DEFAULT_BOLD;
        AssetManager assets = activity.getAssets();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            font = new Typeface.Builder(
                    assets,
                    FONTS_PATH + TYPEFACE_ARIAL_BOLD + FONT_SUFFIX
            ).build();
        }
        return font;
    }
}
