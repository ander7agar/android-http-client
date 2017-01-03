package com.zanjou.http.request;

import com.zanjou.http.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLConnection;

/**
 * Created by ander on 4/07/16.
 */
public class Parameter implements Serializable {

    private static final String TAG = "Parameters";
    private static final long serialVersionUID = -400121795802098801L;

    private String contentType;
    private String nameParam;
    private byte[] paramValue;
    private String filePath;
    private boolean isFile;

    public Parameter(String name, String value) {
        contentType = "Content-Type: text/plain; charset=UTF-8";
        nameParam = name;
        paramValue = value.getBytes();
        isFile = false;
    }

    public Parameter(String name, long value) {
        this(name, String.valueOf(value));
    }

    public Parameter(String name, double value) {
        this(name, String.valueOf(value));
    }

    public Parameter(String name, boolean value) {
        this(name, String.valueOf(value));
    }

    public Parameter(String name, File file) throws IOException {
        contentType = "Content-Type: " + URLConnection.guessContentTypeFromName(file.getName());
        filePath = file.getAbsolutePath();
        nameParam = name;
        isFile = true;
        paramValue = FileUtils.loadFile(file);
    }

    public String getContentType() {
        return contentType;
    }

    public String getNameParam() {
        return nameParam;
    }

    public byte[] getParamValue() {
        return paramValue;
    }

    public String getValueAsString() {
        return new String(getParamValue());
    }

    public boolean isFile() {
        return isFile;
    }

    public File getFile() {
        return new File(filePath);
    }

    @Override
    public String toString() {
        if (isFile) {
            return "K= " + getNameParam() + ", V=" + filePath;
        }
        return "K= " + getNameParam() + ", V=" + new String(getParamValue());
    }
}
