package com.example.personator.shared.data.remote.network;

import android.content.Context;
import android.content.Intent;

import com.example.personator.NewsApp;
import com.example.personator.view.activity.latestNewsActivity.LatestNews;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

import static com.example.personator.shared.di.modules.root.NetModule.checkNetworkConnectionStatus;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public class CallFail extends Throwable {

    private Context context;
    private Throwable error;

    CallFail(Throwable tr) {
        super(tr);
        error = tr;
        context = NewsApp.getInstance();
    }

    public boolean isNetworkError() {
        return isAuthorizationError() || error instanceof SocketTimeoutException;
    }

    public boolean isServerError() {
        return isAuthorizationError() || error instanceof IOException;
    }


    private boolean isAuthorizationError() {
        if (error instanceof HttpException) {
            if (((HttpException) error).code() == HTTP_UNAUTHORIZED || ((HttpException) error).code() == HTTP_FORBIDDEN) {
                Intent intent = new Intent(context, LatestNews.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                return true;
            } else
                return false;
        } else
            return false;
    }

    public String getErrorText() {
        if (!isAuthorizationError()) {
            if (error instanceof SocketTimeoutException)
                return "Sorry something went wrong. Please try again later.";
            if (error instanceof ConnectException && checkNetworkConnectionStatus(context))
                return "Sorry something went wrong. Please try again later.";
            if (error instanceof ConnectException && !checkNetworkConnectionStatus(context))
                return "No internet connection. Please try again later.";
            if (error instanceof IOException)
                return "No internet connection. Please try again later.";
            if (error instanceof HttpException)
                return "Sorry something went wrong. Please try again later.";
        }
        return "Sorry something went wrong. Please try again later.";
    }
}
