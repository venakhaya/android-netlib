package com.android.netlib.view;


import com.android.netlib.base.ResponseListener;

public abstract class BaseViewResponseListener<T> implements ResponseListener<T> {
    private ICallBackView<T> callBack;
    private IProgressView progressView;

    public BaseViewResponseListener(IProgressView progressView, ICallBackView<T> callBack) {
        this.callBack = callBack;
        this.progressView = progressView;
    }

    @Override
    public void onFailure(Throwable throwable) {
        onError(throwable.getMessage());
    }

    @Override
    public void onError(String message) {
        callBack.processError(message);
        progressView.dismissProgressDialog();
    }
}