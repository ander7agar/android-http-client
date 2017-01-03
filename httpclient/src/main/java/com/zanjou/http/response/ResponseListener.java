package com.zanjou.http.response;

/**
 * Created by ander on 4/07/16.
 */
public interface ResponseListener {

    void onResponse(int httpCode, String content);
}
