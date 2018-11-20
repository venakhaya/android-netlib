package com.android.netlib.base;

public interface ResponseListener<T> {
    void onSuccess(T response);
    void onFailure(Throwable throwable);
    void onError(String message);
}
