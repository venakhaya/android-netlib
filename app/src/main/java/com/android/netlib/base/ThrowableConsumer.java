package com.android.netlib.base;

import android.util.Log;

import io.reactivex.functions.Consumer;

public class ThrowableConsumer<V> implements Consumer<Throwable> {
    private final Class<V> responseClass;
    private final ResponseListener<V> listener;
    private final String TAG = this.getClass().getSimpleName();

    public ThrowableConsumer(Class<V> responseClass, ResponseListener<V> listener) {
        this.responseClass = responseClass;
        this.listener = listener;
    }

    @Override
    public void accept(Throwable throwable) {
        Log.e("x-", "throwable " + throwable);
        if (throwable instanceof BaseNetworkInteractor.CustomNetworkException) {
            Log.e(TAG, "ResponseClass=" + responseClass.getSimpleName() + "Custom Throwable-Api-Response: " + throwable.getMessage());
            throwable.printStackTrace();
            listener.onError(throwable.getMessage());
        } else {
            Log.e(TAG, "ResponseClass=" + responseClass.getSimpleName() + "Throwable-Api-Response: " + throwable.getMessage());
            throwable.printStackTrace();
            listener.onFailure(throwable);
        }
        throwable.printStackTrace();
    }
}
