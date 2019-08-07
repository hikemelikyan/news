package com.example.personator.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class Common {
    private Common() {
    }

    public static final String API_KEY = "853259f1-c362-4af3-9d8a-ed881b9ea454";
    public static final String JSON = "json";
    public static final String FIELDS_TO_SHOW = "headline,bodyText,thumbnail,firstPublicationDate";
    public static final String SHARED_PREFERENCES_NAME = "aboutNews";

    public static boolean checkNetworkConnectionStatus(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        } else return false;
    }
}
