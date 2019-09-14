package com.example.personator.shared.data.remote;

import androidx.lifecycle.LiveData;

import com.example.personator.model.Response;
import com.example.personator.model.ResponseModel;
import com.example.personator.model.Result;
import com.example.personator.shared.data.remote.network.ApiResponse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsAPI {

    @GET("search")
    LiveData<ApiResponse<ResponseModel<Response<List<Result>>>>> getContent(@Query("order-by") String orderBy, @Query("page") int pageNumber);

}
