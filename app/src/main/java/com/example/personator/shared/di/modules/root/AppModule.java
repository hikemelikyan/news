package com.example.personator.shared.di.modules.root;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.example.personator.shared.helpers.SharedPreferencesHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application mApp;

    public AppModule(Application mApp) {
        this.mApp = mApp;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApp;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return mApp;
    }

    @Provides
    @Singleton
    Resources providesResources(Context mContext) {
        return mContext.getResources();
    }

    @Provides
    @Singleton
    SharedPreferencesHelper providesSharedPreferencesHelper(Application mApplication) {
        return new SharedPreferencesHelper(mApplication);
    }
}
