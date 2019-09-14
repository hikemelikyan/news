package com.example.personator.shared.di.components.root;

import android.content.Context;

import com.example.personator.shared.di.modules.root.AppModule;
import com.example.personator.shared.di.modules.root.NetModule;
import com.example.personator.shared.helpers.SharedPreferencesHelper;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {

    Retrofit retrofit();

    OkHttpClient okHttpClient();

    Context context();

    SharedPreferencesHelper sharedPreferencesHelper();
}
