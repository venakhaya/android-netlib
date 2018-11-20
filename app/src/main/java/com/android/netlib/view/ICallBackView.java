package com.android.netlib.view;

public interface ICallBackView<T> {
    void processData(T successObject);

    void processError(String errorMessage);
}
