package com.example.personator.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.personator.shared.data.remote.network.CallFail;
import com.hadilq.liveevent.LiveEvent;

import lombok.Getter;

public class BaseViewModel extends AndroidViewModel {

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    @Getter
    private LiveEvent<Boolean> serverErrorLiveData = new LiveEvent<>();
    @Getter
    private LiveEvent<Boolean> networkErrorLiveData = new LiveEvent<>();
    @Getter
    private LiveEvent<String> toastMessageLiveData = new LiveEvent<>();
    @Getter
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
}
