package com.zanjou.http.response;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

/**
 * Created by Andersson G. Acosta on 10/01/17.
 */
public abstract class OkXmlResponseListener extends XmlResponseListener {

    private static final String TAG = "OkResponseListener";


    @Override
    public void onErrorResponse(Document document) {

    }

    @Override
    public void onParseError(Exception e) {
        Log.e(TAG, "Error", e);
    }
}
