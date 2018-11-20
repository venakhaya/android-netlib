package com.android.netlib.view;

public interface IProgressView {
    void showProgressDialog(String... messages);

    void dismissProgressDialog();

    void toastError(int resid);

    void toastError(String resid);
}
