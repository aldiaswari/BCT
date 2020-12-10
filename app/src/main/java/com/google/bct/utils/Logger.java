package com.google.bct.utils;

import android.util.Log;

public class Logger {
    private static String LOG_TAG = Logger.class.getSimpleName();

    public static void d(Exception exc) {
        if (exc != null) {
            Log.d(LOG_TAG, exc.getMessage());
        }
    }

    public static void d(String str) {
        Log.d(LOG_TAG, str);
    }

    public static void d(String str, Exception exc) {
        if (exc != null) {
            Log.d(str, exc.getMessage());
        }
    }

    public static void d(String str, String str2) {
        Log.d(str, str2);
    }

    public static void e(Exception exc) {
        if (exc != null) {
            Log.e(LOG_TAG, exc.getMessage());
        }
    }

    public static void e(String str) {
        Log.e(LOG_TAG, str);
    }

    public static void e(String str, Exception exc) {
        if (exc != null) {
            Log.e(str, exc.getMessage());
        }
    }

    public static void e(String str, String str2) {
        Log.e(str, str2);
    }

    public static void i(Exception exc) {
        if (exc != null) {
            Log.i(LOG_TAG, exc.getMessage());
        }
    }

    public static void i(String str) {
        Log.i(LOG_TAG, str);
    }

    public static void i(String str, Exception exc) {
        if (exc != null) {
            Log.i(str, exc.getMessage());
        }
    }

    public static void i(String str, String str2) {
        Log.i(str, str2);
    }

    public static void v(Exception exc) {
        if (exc != null) {
            Log.v(LOG_TAG, exc.getMessage());
        }
    }

    public static void v(String str) {
        Log.v(LOG_TAG, str);
    }

    public static void v(String str, Exception exc) {
        if (exc != null) {
            Log.v(str, exc.getMessage());
        }
    }

    public static void v(String str, String str2) {
        Log.v(str, str2);
    }

    public static void w(Exception exc) {
        if (exc != null) {
            Log.w(LOG_TAG, exc.getMessage());
        }
    }

    public static void w(String str) {
        Log.w(LOG_TAG, str);
    }

    public static void w(String str, Exception exc) {
        if (exc != null) {
            Log.w(str, exc.getMessage());
        }
    }

    public static void w(String str, String str2) {
        Log.w(str, str2);
    }
}