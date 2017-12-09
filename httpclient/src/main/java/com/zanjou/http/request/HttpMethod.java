package com.zanjou.http.request;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.zanjou.http.request.Request.DELETE;
import static com.zanjou.http.request.Request.GET;
import static com.zanjou.http.request.Request.OPTIONS;
import static com.zanjou.http.request.Request.POST;
import static com.zanjou.http.request.Request.PUT;
import static com.zanjou.http.request.Request.TRACE;

/**
 * Created by ander on 9/12/17.
 */

@StringDef({GET, POST, PUT, DELETE, TRACE, OPTIONS})
@Retention(RetentionPolicy.SOURCE)
public @interface HttpMethod {
}
