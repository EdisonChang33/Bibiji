package com.bibizhaoji.bibiji.server;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.bibizhaoji.bibiji.MainActivity;
import com.bibizhaoji.bibiji.media.MediaHelper;
import com.bibizhaoji.bibiji.support.BlackScene;
import com.bibizhaoji.bibiji.utils.GlobalConfig;
import com.bibizhaoji.bibiji.utils.GlobalConst;
import com.speech.recognizer.IRecognizer;
import com.speech.recognizer.IRecognizerListener;
import com.speech.recognizer.offline.RecognitionListener;
import com.speech.recognizer.offline.RecognitionManager;
import com.bibizhaoji.bibiji.server.support.KeepAliveManager;
import com.bibizhaoji.bibiji.server.support.AliveActivity;
import com.speech.utils.LogEnv;
import com.speech.utils.ThreadUtils;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

/**
 * Created by EdisonChang on 2015/12/16.
 */
public class RecognizerService extends Service implements RecognitionListener {

    private static final String TAG = "RecognizerService";

    private RecognizerBinder mRecognizerBinder;
    private IRecognizerListener mListener;
    private boolean init;
    private boolean startFlag;

    @Override
    public void onCreate() {
        super.onCreate();

        if (LogEnv.enable) {
            Log.d(TAG, TAG + " onCreate");
        }

        startForeground();
        initRecognition();

        if (LogEnv.enable) {
            PushAgent.getInstance(this).setDebugMode(LogEnv.enable);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String device_token = UmengRegistrar.getRegistrationId(RecognizerService.this);
                    Log.d("RecognizerService", "device_token = " + device_token);
                }
            }, 5000);
        }
    }

    private void initRecognition() {
        if (!init) {
            init = true;
            RecognitionManager.init(this);
            RecognitionManager.setRecognitionListener(this);
        }
    }

    /**
     * merge from appstore， 用于提高service优先级， 尽可能不被杀死
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void startForeground() {
        if (Build.VERSION.SDK_INT <= 17) {
            Notification notification = new Notification(0, null, System.currentTimeMillis());
            notification.flags |= Notification.FLAG_NO_CLEAR;
            startForeground(42, notification);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        if (LogEnv.enable) {
            Log.d(TAG, TAG + " onTaskRemoved");
        }

        // 启个空activity
        Intent intent = new Intent(this, AliveActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        //开个AlarmManger启动
        KeepAliveManager.startByAlarmManager(this);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        if (LogEnv.enable) {
            Log.d(TAG, TAG + " onDestroy");
        }
        RecognitionManager.shutdown();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (LogEnv.enable) {
            String type = intent != null ? intent.getStringExtra("type") : "default";
            Log.d(TAG, TAG + " onStartCommand type = " + type);
        }

        if (GlobalConfig.isMainSwitcherOn(this)) {
            initRecognition();
            startRecognizer();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mRecognizerBinder == null)
            mRecognizerBinder = new RecognizerBinder();

        return mRecognizerBinder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        mRecognizerBinder = null;
        super.unbindService(conn);
    }

    @Override
    public void onPartialResults(Bundle b) {
        if (b == null) {
            return;
        }

        int code = RecognizerConfig.STATE_NONE;
        String recResult = b.getString(RecognitionManager.KEY_RECOGNIZER_RESULT);
        if (LogEnv.enable) {
            Log.d(TAG, "********* onPartialResults rec_word:" + recResult);
        }

        if (!TextUtils.isEmpty(recResult)) {
            if (recResult.contains(RecognizerConfig.REC_WORD1)) {
                code = RecognizerConfig.STATE_MATCH;
            } else if (recResult.contains(RecognizerConfig.REC_WORD2)) {
                code = RecognizerConfig.STATE_MARK;
            }
        }

        handleResultCode(code);
        if (mListener != null) {
            try {
                mListener.onResults(code);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleResultCode(int code) {
        if (startFlag && code == RecognizerConfig.STATE_MATCH) {
            if (LogEnv.enable) {
                Log.d(TAG, "********* handleResultCode playSound1 === ");
            }

            stopRecognizer();
            if (BlackScene.phoneIsIdle()) {
                Log.d(TAG, "********* handleResultCode playSound2 === ");

                try {
                    //   MediaHelper.playSound(this, GlobalConfig.RINGTON);
                } catch (Exception e) {
                }

                bringUIToFront();
            }
        }
    }

    private void bringUIToFront() {

        ThreadUtils.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(RecognizerService.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(GlobalConst.EXTRA_STATUS, GlobalConst.STATE_ACTIVE);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public void onResults(Bundle b) {
        if (b == null) {
            return;
        }
        if (LogEnv.enable) {
            String recResult = b.getString(RecognitionManager.KEY_RECOGNIZER_RESULT);
            Log.d(TAG, "********* onPartialResults rec_word:" + recResult);
        }
    }

    @Override
    public void onError(int err) {
        if (mListener != null) {
            try {
                mListener.onError(err);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void startService(Context context, String type) {
        Intent src = new Intent();
        src.setClass(context, RecognizerService.class);
        src.putExtra("type", type);
        src.setPackage(context.getPackageName());
        try {
            context.startService(src);
        } catch (Exception e) {
        }
    }

    private class RecognizerBinder extends IRecognizer.Stub {

        @Override
        public void start() throws RemoteException {
            startRecognizer();
        }

        @Override
        public void stop() throws RemoteException {
            stopRecognizer();
        }

        @Override
        public void shutdown() throws RemoteException {
            RecognitionManager.shutdown();
        }

        @Override
        public void stopSound() throws RemoteException {
            MediaHelper.stopSound();
        }

        @Override
        public int getState() throws RemoteException {
            return MediaHelper.playing ? 1 : 0;
        }

        @Override
        public void registerListener(IBinder token, IRecognizerListener listener) throws RemoteException {
            mListener = listener;
            // 注册客户端死掉的通知
            token.linkToDeath(new ClientState(token), 0);
        }

        @Override
        public void unregisterListener(IBinder token) throws RemoteException {
            mListener = null;
        }
    }

    private synchronized void startRecognizer() {
        startFlag = true;
        RecognitionManager.start();
    }

    private synchronized void stopRecognizer() {
        startFlag = false;
        RecognitionManager.stop();
    }

    private final class ClientState implements IBinder.DeathRecipient {

        public final IBinder mToken;

        public ClientState(IBinder token) {
            mToken = token;
        }

        @Override
        public void binderDied() {
            Log.d(TAG, "client died");
            mListener = null;
        }
    }
}
