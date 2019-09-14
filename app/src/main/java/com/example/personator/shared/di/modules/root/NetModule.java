package com.example.personator.shared.di.modules.root;


import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.personator.BuildConfig;
import com.example.personator.shared.data.remote.network.LiveDataCallAdapterFactory;
import com.example.personator.shared.helpers.SharedPreferencesHelper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.personator.shared.config.Common.API_KEY;
import static com.example.personator.shared.config.Common.FIELDS_TO_SHOW;
import static com.example.personator.shared.config.Common.JSON;
import static com.example.personator.shared.config.Common.PAGE_SIZE;

@Module
public class NetModule {
    private int mMaxStale = 60 * 60 * 24 * 5;

    @Provides
    @Singleton
    Cache providesOkHttpCache(Application application) {
        int cacheSize = 5 * 1024 * 1024;
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    Gson providesGson() {
        GsonBuilder gson = new GsonBuilder();
        gson.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gson.create();
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(Cache cache, Application application) {
        final SharedPreferencesHelper shared = new SharedPreferencesHelper(application);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request originalRequest = chain.request();

            Request request = originalRequest.newBuilder()
                    .addHeader("api-key", shared.getStringSharedPreferences(API_KEY))
                    .addHeader("format", shared.getStringSharedPreferences(JSON))
                    .addHeader("show-fields", shared.getStringSharedPreferences(FIELDS_TO_SHOW))
                    .addHeader("page-size", shared.getStringSharedPreferences(PAGE_SIZE))
                    .addHeader("Cache-control", (checkNetworkConnectionStatus(application)) ?
                            "public,max-age=" + mMaxStale : "public,max-stale=" + mMaxStale)
                    .build();
            Response response = chain.proceed(request);
            response.cacheResponse();
            return response;
        })
                .cache(cache)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }


    @Provides
    @Singleton
    Retrofit providesRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL)
                .client(okHttpClient)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    public static boolean checkNetworkConnectionStatus(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getNetworkInfo(0);
        if (info != null && info.isConnected()) {
            return true;
        } else {
            info = connectivityManager.getNetworkInfo(1);
            if (info != null && info.isConnected()) {
                return true;
            } else return false;
        }
    }
}
