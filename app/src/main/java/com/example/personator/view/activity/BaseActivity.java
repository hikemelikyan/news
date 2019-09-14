package com.example.personator.view.activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.personator.R;
import com.example.personator.viewmodel.BaseViewModel;
import com.google.android.material.snackbar.Snackbar;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements BaseView {


    public <T extends BaseViewModel> T createViewModel(Class<T> viewModelClass, BaseView view) {
        T mViewModel = ViewModelProviders.of(this).get(viewModelClass);
        mViewModel.getNetworkErrorLiveData().observe(this, aBoolean -> view.showNetworkError());
        mViewModel.getServerErrorLiveData().observe(this, aBoolean -> view.showServerError());
        mViewModel.getSnackBarMessageLiveData().observe(this, view::showMessage);
        mViewModel.getToastMessageLiveData().observe(this, view::onError);
        return mViewModel;
    }

    public void checkPermissionSafely(String[] perms, Integer requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, requestCode);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean hasPermission(String perm) {
        return checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }


    @Override
    public void showServerError() {
        showSnackBarError("Sorry something went wrong. Please try again later.");
    }

    @Override
    public void showNetworkError() {
        showSnackBarError("No internet connection. Please try again later.");
    }

    @Override
    public void onError(int resId) {
        onError(getResources().getString(resId));
    }

    @Override
    public void onError(String message) {
        showSnackBarError(message);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(int resId) {
        showMessage(getResources().getString(resId));
    }

    @Override
    public void setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void showSnackBarError(String msg) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.colorPrimaryDark))
                .setTextColor(getResources().getColor(android.R.color.white))
                .show();
    }
}
