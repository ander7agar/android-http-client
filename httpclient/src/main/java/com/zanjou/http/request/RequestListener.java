package com.zanjou.http.request;

/**
 * Created by Andersson G. Acosta on 3/01/17.
 */
public interface RequestListener {

    void onStart();
    void onFinish();
    void onUploadProgress(float progress);
    void onConnectionError(Exception e);
}
