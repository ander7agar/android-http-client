package com.zanjou.http.param;

import android.support.annotation.NonNull;

/**
 * Created by ander on 9/12/17.
 */

public class StringParameter implements Parameter {

    private String key;
    private String value;

    public StringParameter(@NonNull String key, @NonNull String value) {
        this.key = key;
        this.value = value;
    }

    public StringParameter(@NonNull String key, long value) {
        this.key = key;
        this.value = String.valueOf(value);
    }

    public StringParameter(@NonNull String key, double value) {
        this.key = key;
        this.value = String.valueOf(value);
    }

    public StringParameter(@NonNull String key, boolean value) {
        this.key = key;
        this.value = String.valueOf(value);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "K= " + key + ", V=" + value;
    }
}
