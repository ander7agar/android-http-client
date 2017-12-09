package com.zanjou.http.response;

import com.zanjou.http.request.ResponseData;

/**
 * Created by ander on 4/07/16.
 */
public interface ResponseListener {

    void onResponse(ResponseData responseData);
}
