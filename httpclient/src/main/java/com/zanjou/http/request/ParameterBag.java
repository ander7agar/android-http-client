package com.zanjou.http.request;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ander on 4/07/16.
 */
public class ParameterBag extends ArrayList<Parameter> {

    private static final String TAG = "ParameterBag";
    private static final long serialVersionUID = -7245741490129951877L;

    public boolean add(String key, String value) {
        return super.add(new Parameter(key, value));
    }

    public boolean add(String key, long value) {
        return super.add(new Parameter(key, value));
    }

    public boolean add(String key, double value) {
        return super.add(new Parameter(key, value));
    }

    public boolean add(String key, boolean value) {
        return super.add(new Parameter(key, value));
    }

    public boolean add(String key, File value) {
        try {
            return super.add(new Parameter(key, value));
        } catch (IOException e) {
            return false;
        }
    }
}
