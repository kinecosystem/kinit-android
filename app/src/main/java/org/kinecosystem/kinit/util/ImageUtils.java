package org.kinecosystem.kinit.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageUtils {

    private static String imageResolutionPath = null;

    public static void loadImageIntoView(Context context, String serverUrl, ImageView view) {
        String urlWithResolution = urlWithResolution(serverUrl, context);
        Picasso.with(context).load(urlWithResolution).into(view);
    }

    public static void fetchImage(Context context, String url) {
        if (url != null && !url.isEmpty()) {
            String urlWithResolution = urlWithResolution(url, context);
            Picasso.with(context).load(urlWithResolution).fetch();
        }
    }

    public static void fetchImages(Context context, List<String> urls) {
        for (String url : urls) {
            fetchImage(context, url);
        }
    }

    private static String urlWithResolution(String serverUrl, Context context) {
        int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
        if (serverUrl == null || serverUrl.isEmpty()) {
            return serverUrl;
        }
        int i = serverUrl.lastIndexOf("/");
        String imageName = serverUrl.substring(i, serverUrl.length() - 1);
        return serverUrl.replace(imageName, getResolutionString(densityDpi) + imageName);
    }

    private static String getResolutionString(int densityDpi) {
        if (imageResolutionPath != null) {
            return imageResolutionPath;
        }
        if (densityDpi <= DisplayMetrics.DENSITY_MEDIUM) {
            imageResolutionPath = "/android/mdpi";
        } else if (densityDpi <= DisplayMetrics.DENSITY_HIGH) {
            imageResolutionPath = "/android/hdpi";
        } else if (densityDpi <= DisplayMetrics.DENSITY_XHIGH) {
            imageResolutionPath = "/android/xhdpi";
        } else if (densityDpi <= DisplayMetrics.DENSITY_XXHIGH) {
            imageResolutionPath = "/android/xxhdpi";
        } else {
            imageResolutionPath = "/android/xxxhdpi";
        }
        Log.d("ImageUtils", "Density= " + densityDpi + ". Loading images from " + imageResolutionPath);
        return imageResolutionPath;
    }
}
