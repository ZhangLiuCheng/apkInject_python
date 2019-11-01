package com.playin.util;

import android.util.Log;

public class LogUtil {

    private static final String sTAG = "PLAYIN";

    public static boolean DEBUG = true;

    public static void v(String message) {
        if (DEBUG) {
            Log.v(sTAG, "[PlayInJect] " + message);
        }
    }

    public static void i(String message) {
        Log.i(sTAG, "[PlayInJect] " + message);
    }

    public static void d(String message) {
        Log.d(sTAG, "[PlayInJect] " + message);
    }

    public static void e(String message) {
        Log.e(sTAG, "[PlayInJect] " + message);
    }
}
