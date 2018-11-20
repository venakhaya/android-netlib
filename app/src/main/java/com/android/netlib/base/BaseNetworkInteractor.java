package com.android.netlib.base;

import android.util.Log;

import com.androidnetworking.common.ANResponse;

import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseNetworkInteractor implements INetworkInteractor<Object> {
    protected final String TAG = this.getClass().getSimpleName();


    protected <T> Observable<T> createObservable(final String endPoint, final Object request, final Class<T> responseClass) {
        return Observable.fromCallable(new Callable<T>() {
            public T call() throws Exception {
                ANResponse<T> anResponse = NetworkService.execute(getUri() + endPoint, request, responseClass);
                Log.e(TAG, "ResponseClass=" + responseClass.getSimpleName() + "Api-Response: " + anResponse.getOkHttpResponse());
                if (anResponse.isSuccess()) {
                    return anResponse.getResult();
                } else {
                    if (anResponse.getError().getCause().getCause() instanceof SocketTimeoutException) {
                        SocketTimeoutException exception = (SocketTimeoutException) anResponse.getError().getCause().getCause();
                        throw new CustomNetworkException(anResponse.getError().getCause().getCause().getMessage());
                    } else if (anResponse.getError() != null)
                        throw new CustomNetworkException(anResponse.getError().getMessage());
                    else if (anResponse.getOkHttpResponse() != null) {
                        throw new CustomNetworkException(anResponse.getOkHttpResponse().message());
                    } else {
                        throw new CustomNetworkException("Unknown error");
                    }
                }
            }
        });
    }

    @Override
    public <T> void postData(final String endPoint, final Object request, final Class<T> responseClass, final ResponseListener<T> listener) {

        createObservable(endPoint, request, responseClass).subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T response) {
                        listener.onSuccess(response);
                    }
                }, new ThrowableConsumer(responseClass, listener));
    }

    @Override
    public <T> void getData(String endPoint, Object request, Class<T> responseClass, ResponseListener<T> listener) {

    }

    protected String getUri() {
        return "";
    }

    protected final static class CustomNetworkException extends Exception {
        CustomNetworkException(String error) {
            super(error);
        }
    }
}