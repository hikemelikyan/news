package com.example.personator.view.activity;

import androidx.annotation.StringRes;

public interface BaseView {

    void showServerError();

    void showNetworkError();

    void onError(@StringRes int resId);

    void onError(String message);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    void setLightStatusBar();

}
