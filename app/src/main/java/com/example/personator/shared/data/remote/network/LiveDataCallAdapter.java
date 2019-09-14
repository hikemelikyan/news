package com.example.personator.shared.data.remote.network;

import androidx.lifecycle.LiveData;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.HttpException;

public class LiveDataCallAdapter<F> implements CallAdapter<F, LiveData<ApiResponse<F>>> {

    private Type responseType;

    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<ApiResponse<F>> adapt(Call<F> call) {
        return new LiveData<ApiResponse<F>>() {

            AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<F>() {
                        @Override
                        public void onResponse(Call<F> call, retrofit2.Response<F> response) {
                            if (response.isSuccessful()) {
                                postValue(new ApiResponse<F>(response));
                            } else {
                                postValue(new ApiResponse<F>(new HttpException(response)));
                            }
                        }

                        @Override
                        public void onFailure(Call<F> call, Throwable t) {
                            postValue(new ApiResponse<>(t));
                        }
                    });
                }
            }
        };
    }
}
