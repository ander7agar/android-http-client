package com.zanjou.http.response;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 * Created by Andersson G. Acosta on 3/01/17.
 */
public abstract class XmlResponseListener extends BaseResponseListener {

    private static final String TAG = "XmlResponseListener";

    @Override
    public void onErrorResponse(int httpCode, String content) {
        Log.e(TAG, "RESPONSE " + httpCode + ": " + content);
        try {
            Document parse = Jsoup.parse(content);
            onErrorResponse(parse);
        } catch (Exception e) {
            onParseError(e);
        }

    }

    @Override
    public void onOkResponse(String content) {
        try {
            Document doc = Jsoup.parse(content);
            onOkResponse(doc);
        } catch (Exception e) {
            onParseError(e);
        }

    }

    public abstract void onOkResponse(Document document);
    public abstract void onErrorResponse(Document document);
    public abstract void onParseError(Exception e);
}
