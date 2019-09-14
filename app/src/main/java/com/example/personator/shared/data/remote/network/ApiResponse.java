package com.example.personator.shared.data.remote.network;

public class ApiResponse<F> {

    private CallFail callFail;
    private int code;
    private F body;

    ApiResponse(Throwable tr) {
        callFail = new CallFail(tr);
    }

    ApiResponse(retrofit2.Response<F> response) {
        body = response.body();
        code = response.code();
    }

    boolean isCallSuccessful() {
        return code >= 200 && code < 300;
    }

    F getBody() {
        return body;
    }

    public CallFail getCallFail() {
        return callFail;
    }
}
