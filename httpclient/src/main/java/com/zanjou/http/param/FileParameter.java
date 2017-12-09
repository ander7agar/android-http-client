package com.zanjou.http.param;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by ander on 9/12/17.
 */

public class FileParameter implements Parameter {

    private String key;
    private File file;

    public FileParameter(@NonNull String key, @NonNull File file) {
        this.key = key;
        this.file = file;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Object getValue() {
        return file;
    }

    @Override
    public String toString() {
        return "K= " + key + ", V=" + file.getAbsolutePath();
    }
}
