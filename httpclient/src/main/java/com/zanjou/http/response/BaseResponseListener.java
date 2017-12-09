package com.zanjou.http.response;

import android.util.Log;

/**
 * Created by ander on 4/07/16.
 */
public abstract class BaseResponseListener implements ResponseListener {

    private static final String TAG = "BaseResponseListener";

    @Override
    public void onResponse(ResponseData responseData) {
        Log.e(TAG, "RESPONSE " + responseData);
        if (responseData.getResponseCode() >= 200 && responseData.getResponseCode() <= 299) {
            onOkResponse(responseData);
        } else {
            onErrorResponse(responseData);
        }
    }

    public abstract void onErrorResponse(ResponseData responseData);
    public abstract void onOkResponse(ResponseData responseData);
    public void onCancel() {

    }
}
