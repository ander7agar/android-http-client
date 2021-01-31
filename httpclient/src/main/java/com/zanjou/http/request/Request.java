package com.zanjou.http.request;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.zanjou.http.param.HeaderBag;
import com.zanjou.http.debug.Logger;
import com.zanjou.http.param.FileParameter;
import com.zanjou.http.param.Parameter;
import com.zanjou.http.param.ParameterBag;
import com.zanjou.http.param.StringParameter;
import com.zanjou.http.response.ResponseData;
import com.zanjou.http.response.ResponseListener;
import com.zanjou.http.util.ByteStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.methods.HttpDelete;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpOptions;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.client.methods.HttpRequestBase;
import cz.msebera.android.httpclient.client.methods.HttpTrace;
import cz.msebera.android.httpclient.client.utils.URLEncodedUtils;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.conn.ssl.X509HostnameVerifier;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.conn.SingleClientConnManager;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;

import static com.zanjou.http.debug.Logger.DEBUG;
import static com.zanjou.http.debug.Logger.ERROR;
import static com.zanjou.http.debug.Logger.INFO;

/**
 * Created by ander on 4/07/16.
 */
public class Request {

    private static final String TAG = "Request";

    private static final int BUFF_SIZE = 1024;
    private static final String CRLF = "\r\n";

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String TRACE = "TRACE";
    public static final String OPTIONS = "OPTIONS";

    public static final int DEFAULT_TIMEOUT = 60;

    private URL url;

    @HttpMethod
    private String method;
    private StringEntity postEntity;
    private ParameterBag parameters;
    private HeaderBag headers;
    private ResponseListener responseListener;
    private RequestListener requestListener;
    private ProgressTask runner;
    private Logger logger;

    private int timeout = DEFAULT_TIMEOUT * 1000;

    private Request(){
        parameters = new ParameterBag();
        headers = new HeaderBag();
    }

    public URL getUrl() {
        return url;
    }

    public Request setUrl(URL url) {
        this.url = url;
        return this;
    }

    @HttpMethod
    public String getMethod() {
        return method;
    }

    public Request setMethod(@HttpMethod String method) {
        this.method = method.toUpperCase();
        return this;
    }

    public ParameterBag getParameters() {
        return parameters;
    }

    public Request setParameters(ParameterBag parameters) {
        this.parameters = parameters;
        return this;
    }

    public Request addParameters(ParameterBag parameters) {
        this.parameters.addAll(parameters);
        return this;
    }

    public Request addParameter(Parameter parameter) {
        parameters.add(parameter);
        return this;
    }

    public Request addParameter(String key, String value) {
        parameters.add(key, value);
        return this;
    }

    public Request addParameter(String key, boolean value) {
        parameters.add(key, value);
        return this;
    }

    public Request addParameter(String key, long value) {
        parameters.add(key, value);
        return this;
    }

    public Request addParameter(String key, double value) {
        parameters.add(key, value);
        return this;
    }

    public Request addParameter(String key, File value) {
        parameters.add(key, value);
        return this;
    }

    public Request postBody(@NonNull String body) throws UnsupportedEncodingException {
        this.postEntity = new StringEntity(body);
        this.setMethod(POST);
        return this;
    }

    public Request postBody(@NonNull String contentType, @NonNull String body) {
        ContentType ct = ContentType.create(contentType);
        this.postEntity = new StringEntity(body, ct);
        this.setMethod(POST);
        this.addHeader("Content-Type", contentType);
        return this;
    }

    public HeaderBag getHeaders() {
        return headers;
    }

    public Request setHeaders(HeaderBag headers) {
        this.headers = headers;
        return this;
    }

    public Request addHeaders(Collection<? extends Header> headers) {
        this.headers.addAll(headers);
        return this;
    }

    public Request addHeader(String header, String value) {
        headers.add(header, value);
        return this;
    }

    public Request addHeader(String key, boolean value) {
        headers.add(key, value);
        return this;
    }

    public Request addHeader(String key, long value) {
        headers.add(key, value);
        return this;
    }

    public Request addHeader(String key, double value) {
        headers.add(key, value);
        return this;
    }

    public Request setResponseListener(ResponseListener responseListener) {
        this.responseListener = responseListener;
        return this;
    }

    public Request setRequestListener(RequestListener requestListener) {
        this.requestListener = requestListener;
        return this;
    }

    public Request setTimeout(int timeoutInSeconds) {
        this.timeout = timeoutInSeconds * 1000;
        return this;
    }

    public Request setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public void execute() {
        runner = new ProgressTask() {

            @Override
            protected ResponseData doInBackground(Void... params) {

                HostnameVerifier hVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

                DefaultHttpClient httpclient = new DefaultHttpClient();
                SchemeRegistry registry =  new SchemeRegistry();

                SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                socketFactory.setHostnameVerifier((X509HostnameVerifier) hVerifier);
                registry.register(new Scheme("https", socketFactory, 443));
                SingleClientConnManager mgr = new SingleClientConnManager(httpclient.getParams(), registry);
                httpclient = new DefaultHttpClient(mgr, httpclient.getParams());

                // Set verifier
                HttpsURLConnection.setDefaultHostnameVerifier(hVerifier);

                try {
                    logging("URL: " + method + " " + url.toString(), DEBUG);
                    printHeaders();
                    printParams();

                    HttpRequestBase hrb = buildRequest();
                    HttpParams httpParams = httpclient.getParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
                    HttpConnectionParams.setSoTimeout(httpParams, timeout);

                    HttpResponse response = httpclient.execute(hrb);
                    int responseCode = response.getStatusLine().getStatusCode();

                    BufferedInputStream bis;

                    if (response.getEntity() == null) {
                        String jsonString = "{\"status\":\"No server entity.\"}";
                        InputStream is = new ByteArrayInputStream(jsonString.getBytes());
                        bis = new BufferedInputStream(is, BUFF_SIZE);
                    } else {
                        bis = new BufferedInputStream(response.getEntity().getContent(), BUFF_SIZE);
                    }

                    StringBuilder sb = new StringBuilder();
                    byte[] buffer = new byte[BUFF_SIZE];
                    int bytesRead;
                    while ((bytesRead = bis.read(buffer)) > 0) {
                        sb.append(new String(buffer, 0, bytesRead, "UTF-8"));
                    }

                    bis.close();

                    ResponseData responseData = new ResponseData(responseCode, sb.toString().getBytes());
                    return responseData;
                } catch (IOException e) {
                    logging("Error trying to perform request", ERROR, e);
                    if (requestListener != null) {
                        requestListener.onConnectionError(e);
                    }
                } catch (URISyntaxException e) {
                    logging("Error parsing url", ERROR, e);
                }

                return null;
            }
        };

        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private HttpRequestBase buildRequest() throws MalformedURLException, URISyntaxException {
        HttpRequestBase hrb;
        HttpEntity httpEntity;
        if (this.postEntity != null) {
            httpEntity = this.postEntity;
        } else {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            setTextParams(entityBuilder);
            setBinaryParams(entityBuilder);

            httpEntity = entityBuilder.build();
        }

        ProgressHttpEntityWrapper.ProgressCallback progressCallback = new ProgressHttpEntityWrapper.ProgressCallback() {
            @Override
            public void progress(float progress) {
                if (runner != null && !runner.isCancelled()) {
                    runner.publishProgress(progress);
                }
            }
        };

        ProgressHttpEntityWrapper entityWrapper = new ProgressHttpEntityWrapper(httpEntity, progressCallback);

        String url = getUrl().toString();
        String method = getMethod();

        switch (method) {
            case GET:
                hrb = new HttpGet();
                List<NameValuePair> nameValuePairList = new ArrayList<>();
                for (Parameter p : getParameters()) {
                    if (p instanceof StringParameter) {
                        nameValuePairList.add(new BasicNameValuePair(p.getKey(), (String) p.getValue()));
                    }
                }

                if (nameValuePairList.size() > 0) {
                    url = url + "?" +
                            URLEncodedUtils.format(nameValuePairList, "utf-8");
                }
                break;
            case POST:
                HttpPost post = new HttpPost();
                post.setEntity(entityWrapper);
                hrb = post;
                break;
            case PUT:
                HttpPut put = new HttpPut();
                put.setEntity(entityWrapper);
                hrb = put;
                break;
            case DELETE:
                hrb = new HttpDelete();
                break;
            case OPTIONS:
                hrb = new HttpOptions();
                break;
            case TRACE:
                hrb = new HttpTrace();
                break;
            default:
                throw new IllegalArgumentException("Http Method '" + method + "' not supported.");
        }

        setHeaders(hrb);
        hrb.setURI(new URL(url).toURI());
        return hrb;
    }

    private void setHeaders(@NonNull HttpRequestBase hrb) {
        HeaderBag headers = getHeaders();
        hrb.setHeaders(headers.toArray(new Header[]{}));
    }

    private void setTextParams(@NonNull MultipartEntityBuilder entityBuilder) {
        ParameterBag parameters = getParameters();
        for (Parameter p : parameters) {
            if (p instanceof StringParameter) {
                entityBuilder.addTextBody(p.getKey(), (String) p.getValue());
            }
        }
    }

    private void setBinaryParams(@NonNull MultipartEntityBuilder entityBuilder) {
        ParameterBag parameters = getParameters();
        for (Parameter p : parameters) {
            if (p instanceof FileParameter) {
                entityBuilder.addBinaryBody(p.getKey(), (File) p.getValue());
            }
        }
    }

    private void printParams() {
        if (this.postEntity != null) {
            logging("---- POST BODY ----", DEBUG);

            try {
                byte[] postData = ByteStream.toByteArray(this.postEntity.getContent());
                logging(new String(postData), DEBUG);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            logging("---- PARAMETERS ----", DEBUG);

            for (Parameter p : parameters) {
                if (p instanceof FileParameter && method.equalsIgnoreCase(GET)) {
                    logging(p.getKey() + " = IGNORED FILE[" + ((File) p.getValue()).getAbsolutePath() + "]", DEBUG);
                    continue;
                } else if (p instanceof FileParameter) {
                    logging(p.getKey() + " = FILE[" + ((File) p.getValue()).getAbsolutePath() + "]", DEBUG);
                    continue;
                }

                logging(p.getKey() + " = " + p.getValue(), DEBUG);
            }
        }


    }

    public void cancel() {
        if (runner != null) {
            runner.cancel(true);
        }
    }

    private void printHeaders() {
        logging("---- HEADERS ----", DEBUG);
        for (Header h : headers) {
            String key = h.getName();
            if (method.equalsIgnoreCase("GET") && key.equalsIgnoreCase("content-type")) {
                continue;
            }

            logging(key + " = " + h.getValue(), DEBUG);
        }
    }

    private void logging(String message, int level) {
        logging(message, level, null);
    }

    private void logging(String message, int level, Exception e) {
        if (logger != null) {
            switch (level) {
                case INFO:
                    logger.i(TAG, message, e);
                    break;
                case DEBUG:
                    logger.d(TAG, message, e);
                    break;
                default:
                    logger.e(TAG, message, e);
                    break;
            }
        }
    }

    public static Request create(String url) {
        if (!url.contains("https://") && !url.contains("https://")) {
            url = "http://" + url;
        }

        try {
            Request r = new Request();
            r.url = new URL(url);
            return r;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }

    }

    private abstract class ProgressTask extends AsyncTask<Void, Float, ResponseData> {

        public void publishProgress(float progress) {
            super.publishProgress();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (requestListener != null) {
                requestListener.onStart();
            }
        }

        @Override
        protected void onPostExecute(ResponseData responseData) {
            super.onPostExecute(responseData);
            if (responseListener != null && responseData != null) {
                responseListener.onResponse(responseData);
            }

            if (requestListener != null) {
                requestListener.onFinish();
            }
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
        }
    }
}
