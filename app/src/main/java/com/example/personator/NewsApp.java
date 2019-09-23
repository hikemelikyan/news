package com.example.personator;

import android.app.Application;

import com.example.personator.shared.di.components.DaggerMainComponent;
import com.example.personator.shared.di.components.MainComponent;
import com.example.personator.shared.di.components.root.AppComponent;
import com.example.personator.shared.di.components.root.DaggerAppComponent;
import com.example.personator.shared.di.modules.MainModule;
import com.example.personator.shared.di.modules.root.AppModule;
import com.example.personator.shared.di.modules.root.NetModule;

public class NewsApp extends Application {
    private static NewsApp mInstance;
    private AppComponent mAppComponent;
    private MainComponent mMainComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();

        mInstance = this;
    }

    public static synchronized NewsApp getInstance() {
        return mInstance;
    }

    public MainComponent getMainComponent(){
        mMainComponent = DaggerMainComponent.builder()
                .appComponent(mAppComponent)
                .mainModule(new MainModule())
                .build();
        return mMainComponent;
    }

}
