package com.speech.utils;

import android.os.Handler;
import android.os.Looper;


public class ThreadUtils {

    public static long getCurThreadId() {
        return Thread.currentThread().getId();
    }

    public static long getMainThreadId() {
        return Looper.getMainLooper().getThread().getId();
    }


    /**
     * 在UI线程执行Runnable
     *
     * @param action
     */
    public static void runOnUiThread(Runnable action) {
        if (getCurThreadId() == getMainThreadId()) {
            action.run();
        } else {
            new Handler(Looper.getMainLooper()).post(action);
        }
    }

    /**
     * 在UI线程执行Runnable
     *
     * @param action
     */
    public static void postOnUiThread(Runnable action, long delayTime) {
        new Handler(Looper.getMainLooper()).postDelayed(action, delayTime);
    }
}