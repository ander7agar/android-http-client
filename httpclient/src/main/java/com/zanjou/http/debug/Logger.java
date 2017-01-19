package com.zanjou.http.debug;

import android.util.Log;

/**
 * Created by Andersson G. Acosta on 19/01/17.
 */
public class Logger {

    private static final String TAG = "Log";

    public static final int INFO = 0;
    public static final int DEBUG = 1;
    public static final int ERROR = 2;

    private int level;

    public Logger(int level) {
        this.level = level;
    }

    public void i (String tag, String message, Throwable e) {
        if (level >= INFO) {
            if (e != null) {
                Log.i(tag, message, e);
            } else {
                Log.i(tag, message);
            }

        }
    }

    public void d (String tag, String message, Throwable e) {
        if (level >= DEBUG) {
            if (e != null) {
                Log.d(tag, message, e);
            } else {
                Log.d(tag, message);
            }
        }
    }

    public void e (String tag, String message, Throwable e) {
        if (e != null) {
            Log.e(tag, message, e);
        } else {
            Log.e(tag, message);
        }
    }
}
