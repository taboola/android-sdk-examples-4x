package com.taboola.kotlin.examples.screens.web;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class AssetUtil {
    private static final String TAG = AssetUtil.class.getSimpleName();

    /**
     * Responsible for opening and reading asset file into a String
     * @param context Activity {@link Context}
     * @param assetFileName the name of the html used to show Taboola recommendations
     * @return {@code String} of the html file
     */
    public static String getHtmlTemplateFileContent(Context context, String assetFileName) {
        try {
            InputStream inputStream = context.getAssets().open(assetFileName);
            int size = inputStream.available();

            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String contentString = new String(buffer);
            if (TextUtils.isEmpty(contentString)) return "";
            return contentString;
        } catch (IOException e) {
            Log.e(TAG, "getHtmlTemplateFileContent :: error opening template file " + e.toString());
            return "";
        }
    }

}
