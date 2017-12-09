package com.zanjou.http.response;

import com.zanjou.http.request.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ander on 4/07/16.
 */
public abstract class JsonResponseListener extends BaseResponseListener {

    private static final String TAG = "JsonResponseListener";

    @Override
    public void onErrorResponse(ResponseData responseData) {
        try {
            onErrorResponse(new JSONObject(responseData.getDataAsString()));
        } catch (JSONException e) {
            onParseError(e);
        }
    }

    @Override
    public void onOkResponse(ResponseData responseData) {
        try {
            onOkResponse(new JSONObject(responseData.getDataAsString()));
        } catch (JSONException e) {
            onParseError(e);
        }
    }

    public abstract void onOkResponse(JSONObject jsonObject) throws JSONException;
    public abstract void onErrorResponse(JSONObject jsonObject) throws JSONException;
    public abstract void onParseError(JSONException e);
}
