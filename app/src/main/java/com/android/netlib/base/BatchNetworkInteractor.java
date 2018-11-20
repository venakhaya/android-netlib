package com.android.netlib.base;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


public abstract class BatchNetworkInteractor extends BaseNetworkInteractor {
    private List<ObservableWrapper> observableWrappers = new ArrayList<>();
    private Object lock = new Object();
    private BatchCompletedCallBack batchCompletedCallBack;
    private boolean isBatchSuccessful = true;

    public <T> void addToBatch(final String endPoint, final Object request, final Class<T> responseClass, final ResponseListener<T> listener) {
        resetSuccessStatus();
        observableWrappers.add(new ObservableWrapper<>(createObservable(endPoint, request, responseClass),
                responseClass, listener, new ObservableWrapper.CompletedCallBack() {
            @Override
            public void onCompleted(ObservableWrapper wrapper, boolean isSuccess) {
                synchronized (lock) {
                    isBatchSuccessful &= isSuccess;
                    observableWrappers.remove(wrapper);
                    if (observableWrappers.isEmpty()) {
                        batchCompletedCallBack.onCompleted(isBatchSuccessful);
                    }
                }
            }
        }));
    }

    private void resetSuccessStatus() {
        isBatchSuccessful = true;
    }

    @Override
    protected <T> Observable<T> createObservable(final String endPoint, final Object request, final Class<T> responseClass) {
        return super.createObservable(endPoint, request, responseClass);
    }

    public void executeBatch(BatchCompletedCallBack batchCompletedCallBack) {
        this.batchCompletedCallBack = batchCompletedCallBack;
        synchronized (lock) {
            for (ObservableWrapper wrapper : observableWrappers) {
                wrapper.subscribe();
            }
        }
    }

    public interface BatchCompletedCallBack {
        void onCompleted(boolean isSuccess);
    }
}