package com.android.netlib.base;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ObservableWrapper<T> {
    final Observable<T> observable;
    final ResponseListener<T> listener;
    final Class<T> responseClass;
    boolean completed;
    CompletedCallBack completedCallBack;

    public ObservableWrapper(Observable<T> observable, Class<T> responseClass,
                             @NonNull ResponseListener<T> listener, @NonNull CompletedCallBack completedCallBack ) {
        this.observable = observable;
        this.responseClass = responseClass;
        this.listener = listener;
        this.completedCallBack = completedCallBack;
    }

    public void subscribe() {
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<T>() {
            @Override
            public void accept(T response) {
                listener.onSuccess(response);
                completed = true;
                completedCallBack.onCompleted(ObservableWrapper.this, true);
            }
        }, new ThrowableConsumer(responseClass, listener) {
            @Override
            public void accept(Throwable throwable) {
                super.accept(throwable);
                completed = true;
                completedCallBack.onCompleted(ObservableWrapper.this, false);
            }
        });
    }

    public boolean isCompleted() {
        return completed;
    }


    public interface CompletedCallBack {
        void onCompleted(ObservableWrapper wrapper, boolean isSuccess);
    }
}