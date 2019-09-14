package com.example.personator.shared.data.remote.network;

import androidx.lifecycle.Observer;

import com.example.personator.model.Response;
import com.example.personator.model.ResponseModel;

public abstract class ApiCallObserver<F> implements Observer<ApiResponse<ResponseModel<Response<F>>>> {

    public abstract void onSuccess(F response);

    public abstract void onFailure(CallFail callFail);

    @Override
    public void onChanged(ApiResponse<ResponseModel<Response<F>>> responseModelApiResponse) {
        if (responseModelApiResponse.isCallSuccessful() && responseModelApiResponse.getBody() != null) {
            if (responseModelApiResponse.getBody().getResponse().getStatus().equals("ok")) {
                onSuccess(responseModelApiResponse.getBody().getResponse().getResults());
            }
        } else {
            onFailure(responseModelApiResponse.getCallFail());
        }
    }
}
