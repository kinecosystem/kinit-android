package org.kinecosystem.kinit.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkUtils {

    public static boolean isConnected(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }

        final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }
}
