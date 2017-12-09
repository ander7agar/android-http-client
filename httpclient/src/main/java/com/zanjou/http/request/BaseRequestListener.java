package com.zanjou.http.request;

/**
 * Created by Andersson G. Acosta on 3/01/17.
 */
public abstract class BaseRequestListener implements RequestListener {

    private static final String TAG = "BaseRequestListener";

    @Override
    public void onStart() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onUploadProgress(float progress) {

    }

    @Override
    public void onConnectionError(Exception e) {

    }
}
