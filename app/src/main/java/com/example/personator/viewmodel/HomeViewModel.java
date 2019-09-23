package com.example.personator.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.personator.NewsApp;
import com.example.personator.model.Response;
import com.example.personator.model.ResponseModel;
import com.example.personator.model.Result;
import com.example.personator.shared.data.remote.NewsAPI;
import com.example.personator.shared.data.remote.network.ApiCallObserver;
import com.example.personator.shared.data.remote.network.ApiResponse;
import com.example.personator.shared.data.remote.network.CallFail;
import com.example.personator.viewmodel.base.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import lombok.Getter;

public class HomeViewModel extends BaseViewModel {

    @Inject
    NewsAPI mApi;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        NewsApp.getInstance().getMainComponent().inject(this);
    }

    @Getter
    MediatorLiveData<List<Result>> newsLiveData = new MediatorLiveData<>();

    public void getLatestNews() {
        LiveData<ApiResponse<ResponseModel<Response<List<Result>>>>> responce = mApi.getContent("newest", 1);
        newsLiveData.addSource(responce, new ApiCallObserver<List<Result>>() {
            @Override
            public void onSuccess(List<Result> response) {
                newsLiveData.setValue(response);
                newsLiveData.removeSource(responce);
            }

            @Override
            public void onFailure(CallFail callFail) {
                errorSnackbar(callFail.getMessage());
            }
        });
    }
}
