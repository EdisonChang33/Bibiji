package com.speech.recognizer.offline;

import android.content.Context;
import android.util.Log;

import com.speech.utils.LogEnv;
import com.speech.utils.ToastUtils;

/**
 * Created by caiyingyuan on 2015/12/23.
 */
public class RecognitionManager {
    private static String TAG = "RecognitionManager";
    //识别的结果
    public static String KEY_RECOGNIZER_RESULT = "key_recognizer_result";
    private static RecognizerTask recTask;
    private static Thread recThread;

    public static int STATE_INIT = 0;
    public static int STATE_START = 1;
    public static int STATE_STOP = 2;
    public static int STATE_SHUTDOWN = 3;

    public static int RECOGNIZER_STATE;

    private static Context mContext;

    static {
        try {
            System.loadLibrary("pocketsphinx_jni");
            Log.d(TAG, "loaded so");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init(Context context) {
        recTask = new RecognizerTask(context);
        recThread = new Thread(recTask);
        mContext = context;
        RECOGNIZER_STATE = STATE_INIT;
        recThread.start();
    }

    /**
     * 设置识别结果监听器
     */
    public static void setRecognitionListener(RecognitionListener listener) {
        if (recTask != null && listener != null) {
            recTask.setRecognitionListener(listener);
        } else {
            if (LogEnv.enable) {
                ToastUtils.showShort(mContext, "语音识别结果监听器为空");
            }
        }
    }

    public static void start() {
        if (recThread == null || recTask == null || recTask.rl == null) {
            if (LogEnv.enable) {
                ToastUtils.showShort(mContext, "没有设置语音识别结果监听器");
            }
        } else {
            recTask.start();
            RECOGNIZER_STATE = STATE_START;
        }
    }

    public static void stop() {
        recTask.stop();
        RECOGNIZER_STATE = STATE_STOP;
    }

    public static void shutdown() {
        recTask.shutdown();
        RECOGNIZER_STATE = STATE_SHUTDOWN;
    }

}
