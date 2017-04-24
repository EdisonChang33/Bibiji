package com.bibizhaoji.bibiji.client;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.speech.recognizer.IRecognizerListener;
import com.speech.recognizer.offline.RecognitionManager;
import com.speech.utils.LogEnv;

/**
 * Created by EdisonChang on 2015/12/17.
 */
public class RecognizerDelegate {

    private static final String TAG = "RecognizerDelegate";
    private static RecognizerListener listener;

    public static void init(Context context) {
        if (listener == null) {
            listener = new RecognizerListener();
        }
        RecognizerClient.getInstance().registerListener(context.getApplicationContext(), listener);
    }

    public static void unInit(Context context) {
        RecognizerClient.getInstance().unregisterListener(context.getApplicationContext());
    }

    public static void start(Context context) {
        RecognizerClient.getInstance().start(context.getApplicationContext());
    }

    public static void stop(Context context) {
        RecognizerClient.getInstance().stop(context.getApplicationContext());
    }

    public static void shutdown(Context context) {
        RecognizerClient.getInstance().shutdown(context.getApplicationContext());
    }

    public static void stopSound(Context context) {
        RecognizerClient.getInstance().stopSound(context.getApplicationContext());
    }

    public static int getState() {
        return RecognizerClient.getInstance().getState();
    }

    private static class RecognizerListener extends IRecognizerListener.Stub {

        @Override
        public void onResults(final int code) throws RemoteException {
            if (LogEnv.enable) {
                Log.d(TAG, "onResults onResults = " + code);
            }
        }

        @Override
        public void onError(int err) throws RemoteException {
            if (LogEnv.enable) {
                Log.d(TAG, "onError err = " + err);
            }
        }
    }

}
