package com.example.personator.view.activity;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.personator.viewmodel.BaseViewModel;

public class BaseFragment extends Fragment implements BaseView {

    private BaseActivity mActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity)
            this.mActivity = (BaseActivity) context;
    }

    public <T extends BaseViewModel> T createViewModel(Class<T> viewModelClass, BaseView view) {
        T viewModel = ViewModelProviders.of(this).get(viewModelClass);
        viewModel.getNetworkErrorLiveData().observe(this, aBoolean -> view.showNetworkError());
        viewModel.getServerErrorLiveData().observe(this, aBoolean -> view.showServerError());
        viewModel.getSnackBarMessageLiveData().observe(this, view::showMessage);
        viewModel.getToastMessageLiveData().observe(this, view::onError);
        return viewModel;
    }

    @Override
    public void showServerError() {
        if (mActivity != null) {
            mActivity.showServerError();
        }
    }

    @Override
    public void showNetworkError() {
        if (mActivity != null) {
            mActivity.showNetworkError();
        }
    }

    @Override
    public void onError(int resId) {
        if (mActivity != null) {
            mActivity.onError(resId);
        }
    }

    @Override
    public void onError(String message) {
        if (mActivity != null) {
            mActivity.onError(message);
        }
    }

    @Override
    public void showMessage(String message) {
        if (mActivity != null) {
            mActivity.showMessage(message);
        }
    }

    @Override
    public void showMessage(int resId) {
        if (mActivity != null) {
            mActivity.showMessage(resId);
        }

    }

    @Override
    public void setLightStatusBar() {
        if (mActivity != null) {
            mActivity.setLightStatusBar();
        }
    }
}
