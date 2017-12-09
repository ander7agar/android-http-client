package com.zanjou.http.response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 * Created by Andersson G. Acosta on 3/01/17.
 */
public abstract class XmlResponseListener extends BaseResponseListener {

    private static final String TAG = "XmlResponseListener";

    @Override
    public void onErrorResponse(ResponseData responseData) {
        try {
            Document parse = Jsoup.parse(responseData.getDataAsString());
            onErrorResponse(parse);
        } catch (Exception e) {
            onParseError(e);
        }

    }

    @Override
    public void onOkResponse(ResponseData responseData) {
        try {
            Document doc = Jsoup.parse(responseData.getDataAsString());
            onOkResponse(doc);
        } catch (Exception e) {
            onParseError(e);
        }

    }

    public abstract void onOkResponse(Document document);
    public abstract void onErrorResponse(Document document);
    public abstract void onParseError(Exception e);
}
