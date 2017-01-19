package com.zanjou.httpclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zanjou.http.debug.Logger;
import com.zanjou.http.request.FileUploadListener;
import com.zanjou.http.request.Parameter;
import com.zanjou.http.request.Request;
import com.zanjou.http.request.RequestStateListener;
import com.zanjou.http.response.JsonResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Request request = Request.create("https://activismap-api.owldevelopers.com/v1/public/search");
        request.setMethod(Request.GET)
                .setTimeout(120) //2 Minutes
                .addParameter("area", "69.5754653691267,63.281267769634724@-69.57545004180163,-63.281245976686485")
                .setLogger(new Logger(Logger.ERROR))
                .setFileUploadListener(new FileUploadListener() {
                    @Override
                    public void onUploadingFile(File file, long size, long uploaded) {

                    }
                })
                .setRequestStateListener(new RequestStateListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onConnectionError(Exception e) {
                        e.printStackTrace();
                    }
                })
                .setResponseListener(new JsonResponseListener() {
                    @Override
                    public void onOkResponse(JSONObject jsonObject) throws JSONException {

                    }

                    @Override
                    public void onErrorResponse(JSONObject jsonObject) throws JSONException {

                    }

                    @Override
                    public void onParseError(JSONException e) {

                    }
                }).execute();
    }
}
