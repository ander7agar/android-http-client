package com.zanjou.http.request;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.zanjou.http.common.Header;
import com.zanjou.http.common.HeaderBag;
import com.zanjou.http.debug.Logger;
import com.zanjou.http.response.FileDownloadListener;
import com.zanjou.http.response.FileResponseListener;
import com.zanjou.http.response.ResponseListener;
import com.zanjou.http.util.ByteStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Map;

import static com.zanjou.http.debug.Logger.DEBUG;
import static com.zanjou.http.debug.Logger.ERROR;
import static com.zanjou.http.debug.Logger.INFO;

/**
 * Created by ander on 4/07/16.
 */
public class Request {

    private static final String TAG = "Request";

    private static final String CRLF = "\r\n";

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String OPTIONS = "OPTIONS";
    public static final String HEAD = "HEAD";
    public static final String TRACE = "TRACE";

    public static final int DEFAULT_TIMEOUT = 60;

    private URL url;
    private String method;
    private ParameterBag parameters;
    private HeaderBag headers;
    private String boundary;
    private ResponseListener responseListener;
    private FileUploadListener fileUploadListener;
    private FileDownloadListener fileDownloadListener;
    private RequestStateListener requestStateListener;
    private ProgressTask runner;
    private Logger logger;

    private int timeout = DEFAULT_TIMEOUT * 1000;

    private Request(){
        boundary = Long.toHexString(System.currentTimeMillis());
        parameters = new ParameterBag();
        headers = new HeaderBag();
        headers.add("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    public String getBoundary() {
        return boundary;
    }

    public URL getUrl() {
        return url;
    }

    public Request setUrl(URL url) {
        this.url = url;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public Request setMethod(String method) {
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

    public Request setFileUploadListener(FileUploadListener fileUploadListener) {
        this.fileUploadListener = fileUploadListener;
        return this;
    }

    public Request setFileDownloadListener(FileDownloadListener fileDownloadListener) {
        this.fileDownloadListener = fileDownloadListener;
        return this;
    }

    public Request setRequestStateListener(RequestStateListener requestStateListener) {
        this.requestStateListener = requestStateListener;
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

            boolean fireOnFinish = true;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (requestStateListener != null) {
                    requestStateListener.onStart();
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (requestStateListener != null && fireOnFinish) {
                    requestStateListener.onFinish();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {

                try {

                    String method = getMethod();

                    String finalUrl = parseWithGet();

                    URL finalURL = new URL(finalUrl);

                    HttpURLConnection connection = (HttpURLConnection) finalURL.openConnection();
                    connection.setConnectTimeout(timeout);

                    connection.setDoOutput(!method.equals(GET));
                    connection.setRequestMethod(getMethod());

                    logging("URL: " + method + " " + url.toString(), DEBUG);

                    sendHeaders(connection);
                    printParams();

                    if (!method.equals(GET)) {
                        OutputStream outputStream = connection.getOutputStream();
                        sendParams(outputStream);
                    }

                    if (responseListener != null) {
                        int responseCode = connection.getResponseCode();
                        byte[] data;
                        if (200 >= connection.getResponseCode() && connection.getResponseCode() <= 299) {
                            if (responseListener instanceof FileResponseListener) {
                                downloadFile(connection);
                                return null;
                            } else {
                                data = ByteStream.toByteArray(connection.getInputStream());
                            }
                        } else {
                            data = ByteStream.toByteArray(connection.getErrorStream());
                        }

                        String response = new String(data);

                        logging("Response: " + response, DEBUG);

                        publishProgress(responseCode, response);
                    }
                    connection.disconnect();
                } catch (IOException e) {
                    logging("Error trying to perform request", ERROR, e);
                    fireOnFinish = false;
                    if (requestStateListener != null) {
                        requestStateListener.onConnectionError(e);
                    }
                }

                return null;
            }
        };

        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private String parseWithGet() {
        String method = getMethod();

        if (method == null) {
            method = GET;
        }

        if (!method.equalsIgnoreCase(GET)) {
            return url.toString();
        }

        Uri uri = Uri.parse(url.toString());
        Uri.Builder uriBuilder = uri.buildUpon();

        ParameterBag parameters = getParameters();

        for (Parameter p : parameters) {
            if (p.isFile()) {
                continue;
            }

            uriBuilder.appendQueryParameter(p.getNameParam(), p.getValueAsString());
        }

        return uriBuilder.build().toString();
    }

    private void printParams() {
        logging("---- PARAMETERS ----", DEBUG);

        for (Parameter p : parameters) {
            if (p.isFile() && method.equalsIgnoreCase(GET)) {
                logging(p.getNameParam() + " = IGNORED FILE[" + p.getFile().getAbsolutePath() + "]", DEBUG);
                continue;
            } else if (p.isFile()) {
                logging(p.getNameParam() + " = FILE[" + p.getFile().getAbsolutePath() + "]", DEBUG);
                continue;
            }

            logging(p.getNameParam() + " = " + new String(p.getParamValue()), DEBUG);
        }

    }

    private void downloadFile(HttpURLConnection connection) throws IOException {
        int fileLength = connection.getContentLength();
        int bufferSize = fileLength / 100;
        if (bufferSize <= 0) {
            bufferSize = 1;
        }

        FileResponseListener fileListener = (FileResponseListener) responseListener;
        File downloadFile = fileListener.getFile();
        OutputStream output  = new FileOutputStream(downloadFile);

        InputStream input = connection.getInputStream();

        byte[] fileData = new byte[bufferSize];
        long total = 0;
        int count;

        if (fileDownloadListener != null) {
            fileDownloadListener.onDownloadStart();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        while ((count = input.read(fileData)) != -1) {
            if (runner.isCancelled()) {
                input.close();
                output.close();
                if (fileDownloadListener != null) {
                    fileDownloadListener.onDownloadCancel();
                }

                fileListener.onCancel();
                return;
            }

            total += count;
            baos.write(fileData, 0, count);

            if (fileDownloadListener != null) {
                fileDownloadListener.onDownloadingFile(downloadFile, fileLength, total);
            }
        }

        byte[] data = baos.toByteArray();
        output.write(data);
        output.flush();
        output.close();

        if (!runner.isCancelled()) {
            if (fileDownloadListener != null) {
                fileDownloadListener.onDownloadFinish();
            }
            responseListener.onResponse(200, new String(data));
        }

    }

    public void cancel() {
        if (runner != null) {
            runner.cancel(true);
        }
    }

    private void sendParams(OutputStream outputStream) throws IOException {
        if (!parameters.isEmpty()) {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            for (Parameter p : parameters) {

                writer.append("--").append(boundary).append(CRLF);

                writer.append(p.getContentType()).append(CRLF);

                if (p.isFile()) {
                    writer.append("Content-Disposition: form-data; name=\"")
                            .append(p.getNameParam())
                            .append("\"; filename=\"")
                            .append(p.getFile().getName())
                            .append("\"")
                            .append(CRLF).flush();

                    writer.append(p.getContentType())
                            .append(CRLF);

                    writer.append("Content-Transfer-Encoding: binary")
                            .append(CRLF);

                    writer.flush();

                    byte[] data = p.getParamValue();

                    if (fileUploadListener != null) {
                        fileUploadListener.onUploadStart();
                    }

                    ByteArrayInputStream bais = new ByteArrayInputStream(data);

                    int bufferSize = data.length / 100;
                    if (bufferSize <= 0) {
                        bufferSize = 1;
                    }
                    byte[] buff = new byte[bufferSize];

                    int lenght;
                    long progress = 0;
                    while ((lenght = bais.read(buff)) != -1 ) {

                        if (runner.isCancelled()) {
                            bais.close();
                            outputStream.close();
                            if (fileUploadListener != null) {
                                fileUploadListener.onUploadCancel();
                            }
                            return;
                        }
                        outputStream.write(buff, 0, lenght);
                        progress += lenght;
                        runner.publishProgress2((long) data.length, progress, p.getFile());
                    }

                    outputStream.flush();

                    if (fileUploadListener != null) {
                        fileUploadListener.onUploadFinish(p.getFile());
                    }

                } else {
                    if (!method.equals(GET)) {
                        writer.append("Content-Disposition: form-data; name=\"")
                                .append(p.getNameParam())
                                .append("\"").append(CRLF);
                        writer.append(p.getContentType())
                                .append(CRLF);
                        writer.append(CRLF).append(p.getValueAsString());
                    }

                }

                writer.append(CRLF).flush();
            }
            writer.append("--").append(boundary).append("--").append(CRLF).flush();
        }

    }

    private void sendHeaders(URLConnection connection) {
        logging("---- HEADERS ----", DEBUG);
        for (Header h : headers) {
            String key = h.getKey();
            if (method.equalsIgnoreCase("GET") && key.equalsIgnoreCase("content-type")) {
                continue;
            }

            logging(key + " = " + h.getValue(), DEBUG);

            connection.addRequestProperty(key, h.getValue());
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
        if (!url.contains("https://") && url.contains("https://")) {
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




    private abstract class ProgressTask extends AsyncTask<Void, Object, Void> {

        protected void publishProgress2(Object... values) {
            long length = Long.parseLong(values[0].toString());
            long progress = Long.parseLong(values[1].toString());
            File f = (File) values[2];
            if (fileUploadListener != null) {
                Log.e(TAG, "uploading " + progress + " / " + length);
                fileUploadListener.onUploadingFile(f, length, progress);
            }
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);

            if (responseListener != null) {
                int responseCode = Integer.parseInt(values[0].toString());
                String response = values[1].toString();
                responseListener.onResponse(responseCode, response);
            }
        }
    }
}
