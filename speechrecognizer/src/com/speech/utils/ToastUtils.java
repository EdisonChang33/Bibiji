package com.speech.utils;

import android.content.Context;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * @author jinmeng on 16-5-11.
 *         Toast统一控制类
 */
public class ToastUtils {

    private static WeakReference<Toast> sToastRef;

    private ToastUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static final boolean isShow = true;

    public static void showShort(Context context, CharSequence message) {
        if (isShow) {
            showToast(context, message, Toast.LENGTH_SHORT);
        }
    }

    public static void showShort(Context context, int message) {
        if (isShow) {
            showToast(context, context.getString(message), Toast.LENGTH_SHORT);
        }
    }

    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            showToast(context, message, Toast.LENGTH_LONG);
        }
    }

    public static void showLong(Context context, int message) {
        if (isShow) {
            showToast(context, context.getString(message), Toast.LENGTH_LONG);
        }
    }

    public static void show(Context context, CharSequence message, int duration) {
        if (isShow) {
            showToast(context, message, duration);
        }
    }

    public static void show(Context context, int message, int duration) {
        if (isShow) {
            showToast(context, context.getString(message), duration);
        }
    }

    private static void hideToast() {
        if (sToastRef != null) {
            Toast previousToast = sToastRef.get();
            if (previousToast != null) {
                previousToast.cancel();
            }
        }
    }

    private static void showToast(final Context context, final CharSequence msg, final int duration) {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context.getApplicationContext(), msg, duration);
                hideToast();
                toast.show();
                sToastRef = new WeakReference<>(toast);
            }
        });
    }
}
