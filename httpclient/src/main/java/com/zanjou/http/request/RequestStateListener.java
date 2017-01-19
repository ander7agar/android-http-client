package com.zanjou.http.request;

/**
 * Created by Andersson G. Acosta on 3/01/17.
 */
public interface RequestStateListener {

    void onStart();
    void onFinish();
    void onConnectionError(Exception e);
}
