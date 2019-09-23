package com.example.personator.shared.di.modules;

import com.example.personator.shared.data.remote.NewsAPI;
import com.example.personator.shared.di.scopes.MainScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {
    @Provides
    @MainScope
    NewsAPI providesNewsApi(Retrofit retrofit){
        return retrofit.create(NewsAPI.class);
    }
}
