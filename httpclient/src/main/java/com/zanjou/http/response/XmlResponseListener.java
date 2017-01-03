package com.zanjou.http.response;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Andersson G. Acosta on 3/01/17.
 */
public abstract class XmlResponseListener extends BaseResponseListener {

    private static final String TAG = "XmlResponseListener";

    @Override
    public void onErrorResponse(int httpCode, String content) {
        Log.e(TAG, "RESPONSE " + httpCode + ": " + content);
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document parse = documentBuilder.parse(new ByteArrayInputStream(content.getBytes()));
            onErrorResponse(parse);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            onParseError(e);
        }
    }

    @Override
    public void onOkResponse(String content) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document parse = documentBuilder.parse(new ByteArrayInputStream(content.getBytes()));
            onOkResponse(parse);
        } catch (SAXException | ParserConfigurationException | IOException e) {
            onParseError(e);
        }
    }

    public abstract void onOkResponse(Document document);
    public abstract void onErrorResponse(Document document);
    public abstract void onParseError(Exception e);
}
