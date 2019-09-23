package com.example.personator.viewmodel.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.personator.shared.data.remote.network.CallFail;
import com.hadilq.liveevent.LiveEvent;

public class BaseViewModel extends AndroidViewModel {

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    private LiveEvent<Boolean> serverErrorLiveData = new LiveEvent<>();
    private LiveEvent<Boolean> networkErrorLiveData = new LiveEvent<>();
    private LiveEvent<String> toastMessageLiveData = new LiveEvent<>();
    private LiveEvent<String> snackBarMessageLiveData = new LiveEvent<>();

    public void errorSnackbar(String message) {
        snackBarMessageLiveData.setValue(message);
    }

    public void errorToast(String msg) {
        toastMessageLiveData.setValue(msg);
    }

    public void getMessage(CallFail callFail) {
        if (callFail.isNetworkError())
            networkErrorLiveData.setValue(true);
        else if (callFail.isServerError())
            serverErrorLiveData.setValue(true);
        else serverErrorLiveData.setValue(true);
    }

    public LiveEvent<Boolean> getServerErrorLiveData() {
        return serverErrorLiveData;
    }

    public LiveEvent<Boolean> getNetworkErrorLiveData() {
        return networkErrorLiveData;
    }

    public LiveEvent<String> getToastMessageLiveData() {
        return toastMessageLiveData;
    }

    public LiveEvent<String> getSnackBarMessageLiveData() {
        return snackBarMessageLiveData;
    }
}
