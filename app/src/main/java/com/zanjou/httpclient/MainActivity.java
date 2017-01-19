package com.zanjou.httpclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

        Request request = Request.create("http://service.server.com/getData");
        request.setMethod("POST")
                .setTimeout(120) //2 Minutes
                .addHeader("Authorization", "Key=MY_SERVICE_KEY")
                .addParameter("key1", "value1")
                .addParameter("key2", "value3")
                .addParameter(new Parameter("key3", "value3"))
                .addParameter("file", new File(""))
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
