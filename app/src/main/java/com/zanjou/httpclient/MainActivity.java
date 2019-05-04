package com.zanjou.httpclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.zanjou.http.debug.Logger;
import com.zanjou.http.request.Request;
import com.zanjou.http.request.RequestListener;
import com.zanjou.http.response.XmlResponseListener;

import org.jsoup.nodes.Document;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView response = (TextView) findViewById(R.id.response);
        Request request = Request.create("http://www.google.com");
        request.setMethod(Request.GET)
                .setTimeout(120) //2 Minutes
                .setLogger(new Logger(Logger.ERROR))
                .setRequestListener(new RequestListener() {
                    @Override
                    public void onStart() {
                        Log.d(TAG, "onStart");
                    }

                    @Override
                    public void onFinish() {
                        Log.d(TAG, "onFinish");
                    }

                    @Override
                    public void onUploadProgress(float progress) {
                        Log.d(TAG, "onProgress " + progress);
                    }

                    @Override
                    public void onConnectionError(Exception e) {
                        e.printStackTrace();
                    }
                })
                .setResponseListener(new XmlResponseListener() {
                    @Override
                    public void onOkResponse(Document document) {
                        response.setText(document.text());
                    }

                    @Override
                    public void onErrorResponse(Document document) {
                        response.setText(document.text());
                    }

                    @Override
                    public void onParseError(Exception e) {
                        e.printStackTrace();
                    }
                }).execute();
    }
}
