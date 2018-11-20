package com.android.netlib.base;

public interface INetworkInteractor<R> {
     <T> void postData(final String endPoint, final R request, final Class<T> responseClass, final ResponseListener<T> listener);
     <T> void getData(final String endPoint, final R request, final Class<T> responseClass, final ResponseListener<T> listener);
}