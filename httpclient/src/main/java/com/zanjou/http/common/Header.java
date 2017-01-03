package com.zanjou.http.common;

import java.io.Serializable;

/**
 * Created by Andersson G. Acosta on 3/01/17.
 */
public class Header implements Serializable {

    private static final String TAG = "Header";

    private String key, value;

    public Header(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Header(String name, long value) {
        this(name, String.valueOf(value));
    }

    public Header(String name, double value) {
        this(name, String.valueOf(value));
    }

    public Header(String name, boolean value) {
        this(name, String.valueOf(value));
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Header) {
            Header h = (Header) obj;
            return h.key.toLowerCase().equals(this.key.toLowerCase());
        }

        return false;
    }
}
