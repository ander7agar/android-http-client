package com.zanjou.http.response;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ander on 4/07/16.
 */
public abstract class JsonResponseListener extends BaseResponseListener {

    private static final String TAG = "JsonResponseListener";

    @Override
    public void onErrorResponse(int httpCode, String content) {
        Log.e(TAG, "RESPONSE " + httpCode + ": " + content);
        try {
            onErrorResponse(new JSONObject(content));
        } catch (JSONException e) {
            onParseError(e);
        }
    }

    @Override
    public void onOkResponse(String content) {
        try {
            onOkResponse(new JSONObject(content));
        } catch (JSONException e) {
            onParseError(e);
        }
    }

    public abstract void onOkResponse(JSONObject jsonObject) throws JSONException;
    public abstract void onErrorResponse(JSONObject jsonObject) throws JSONException;
    public abstract void onParseError(JSONException e);
}
