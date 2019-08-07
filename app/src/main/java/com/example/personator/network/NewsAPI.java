package com.example.personator.network;

import com.example.personator.model.News;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsAPI {

    @GET("search")
    Observable<News> getContent(
            @Query("format") String format,
            @Query("order-by") String orderBy,
            @Query("show-fields") String fieldsToShow,
            @Query("page-size") int pageSize,
            @Query("page") int pageNumber,
            @Query("api-key") String key);

}
