
package com.dfjy.seal.util;

import android.util.Log;

public final class MyLog {

    private static volatile boolean DISABLED = false;

    private MyLog() {
    }

    /** Enables logger (if {@link #disableLogging()} was called before) */
    public static void enableLogging() {
        DISABLED = false;
    }

    /**
     * Disables logger, no logs will be passed to LogCat, all log methods will
     * do nothing
     */
    public static void disableLogging() {
        DISABLED = true;
    }

    public static void d(String tag, String message) {
        if (DISABLED)
            return;
        log(Log.DEBUG, tag, message);
    }

    public static void i(String tag, String message) {
        if (DISABLED)
            return;
        log(Log.INFO, message, message);
    }

    public static void w(String tag, String message) {
        if (DISABLED)
            return;
        log(Log.WARN, message, message);
    }

    public static void e(String tag, String message) {
        if (DISABLED)
            return;
        log(Log.ERROR, message, message);
    }

    private static void log(int priority, String tag, String message) {
        if (DISABLED)
            return;
        Log.println(priority, tag, message);
    }

}
