package com.android.netlib.base;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.common.Priority;
import com.jacksonandroidnetworking.JacksonParserFactory;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class NetworkService {
    private final static JacksonParserFactory jaksonParser = new JacksonParserFactory();
    private final static OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .writeTimeout(16, TimeUnit.SECONDS)
            .readTimeout(32, TimeUnit.SECONDS)
            .build();


    public static <T> ANResponse<T> execute(String url, Object requestObject, Class<T> responseClass) {
        AndroidNetworking.setParserFactory(jaksonParser);
        ANResponse<T> anResponse = AndroidNetworking.post(url)
                .addBodyParameter(requestObject) // posting java object
                .setTag(requestObject.getClass().getName())
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .executeForObject(responseClass);
        Log.d("x-i", String.format(Locale.US, "[%s, %s]", requestObject.getClass().getSimpleName(), responseClass.getSimpleName()));
        return anResponse;
    }
    public static <T> ANResponse<T> executeGet(String url, Object requestObject, Class<T> responseClass) {
        AndroidNetworking.setParserFactory(jaksonParser);
        ANResponse<T> anResponse = AndroidNetworking.get(url)
                .setTag(requestObject.getClass().getName())
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .executeForObject(responseClass);
        Log.d("x-i", String.format(Locale.US, "[%s, %s]", requestObject.getClass().getSimpleName(), responseClass.getSimpleName()));
        return anResponse;
    }
}