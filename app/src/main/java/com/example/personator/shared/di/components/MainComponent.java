package com.example.personator.shared.di.components;

import com.example.personator.shared.di.components.root.AppComponent;
import com.example.personator.shared.di.modules.MainModule;
import com.example.personator.shared.di.scopes.MainScope;
import com.example.personator.viewmodel.HomeViewModel;

import dagger.Component;

@MainScope
@Component(dependencies = {AppComponent.class}, modules = {MainModule.class})
public interface MainComponent {
    void inject(HomeViewModel homeViewModel);
}
