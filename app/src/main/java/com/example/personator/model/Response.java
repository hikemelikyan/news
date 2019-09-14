package com.example.personator.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<T> {

    private String status;
    private String userTier;
    private long total;
    private long startIndex;
    private long pageSize;
    private long currentPage;
    private long pages;
    private String orderBy;
    private T results = null;

}
