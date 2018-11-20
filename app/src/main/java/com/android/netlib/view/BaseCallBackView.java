package com.android.netlib.view;

public abstract class BaseCallBackView<T> implements ICallBackView<T> {
    IProgressView view;

    public BaseCallBackView(IProgressView progressView) {
        this.view = progressView;
    }

    @Override
    public void processError(String message) {
        if (message != null) {
            view.toastError(message);
        }
    }
}
