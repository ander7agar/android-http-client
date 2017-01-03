package com.zanjou.http.response;

/**
 * Created by ander on 4/07/16.
 */
public abstract class BaseResponseListener implements ResponseListener {

    private static final String TAG = "BaseResponseListener";

    @Override
    public void onResponse(int httpCode, String content) {
        if (httpCode >= 200 && httpCode <= 299) {
            onOkResponse(content);
        } else {
            onErrorResponse(httpCode, content);
        }
    }

    public abstract void onErrorResponse(int httpCode, String content);
    public abstract void onOkResponse(String content);
    public void onCancel() {

    }
}
