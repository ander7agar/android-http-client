package com.zanjou.http.param;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by Andersson G. Acosta on 3/01/17.
 */
public class HeaderBag extends ArrayList<Header> {

    private static final String TAG = "HeaderBag";

    public boolean add(String key, String value) {
        return super.add(new BasicHeader(key, value));
    }

    public boolean add(String key, long value) {
        return add(new BasicHeader(key, String.valueOf(value)));
    }

    public boolean add(String key, double value) {
        return add(new BasicHeader(key, String.valueOf(value)));
    }

    public boolean add(String key, boolean value) {
        return add(new BasicHeader(key, String.valueOf(value)));
    }

    @Override
    public boolean add(Header header) {
        if (contains(header)) {
            int index = indexOf(header);
            remove(index);
        }

        return super.add(header);
    }
}
