package com.zanjou.http.response;

import android.support.annotation.NonNull;

/**
 * Created by ander on 9/12/17.
 */

public class ResponseData {

    private int responseCode;
    private byte[] data;

    public ResponseData(int responseCode, @NonNull byte[] data) {
        this.responseCode = responseCode;
        this.data = data;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public byte[] getData() {
        return data;
    }

    public String getDataAsString() {
        return new String(data);
    }

    @Override
    public String toString() {
        return responseCode + ": " + getDataAsString();
    }
}
