package com.bibizhaoji.bibiji.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.bibizhaoji.bibiji.base.BaseRemoteService;
import com.bibizhaoji.bibiji.server.RecognizerService;
import com.speech.recognizer.IRecognizer;
import com.speech.recognizer.IRecognizerListener;

/**
 * Created by EdisonChang on 2015/12/16.
 */
public class RecognizerClient extends BaseRemoteService {

    private IRecognizer mService;
    private final IBinder mToken = new Binder();

    private static final RecognizerClient instance = new RecognizerClient();

    public static RecognizerClient getInstance() {
        return instance;
    }

    @Override
    protected void onServiceConnected(ComponentName name, IBinder service) {
        mService = IRecognizer.Stub.asInterface(service);
    }

    @Override
    protected void onServiceDisconnected(ComponentName name) {
        mService = null;
    }

    @Override
    protected boolean isServiceBindSuc() {
        return mService != null;
    }

    @Override
    protected Intent getBindServiceIntent(Context context) {
        return new Intent(context, RecognizerService.class);
    }

    public void start(Context context) {
        postPendingRunnable(context, new Runnable() {
            @Override
            public void run() {
                try {
                    mService.start();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void stop(Context context) {
        postPendingRunnable(context, new Runnable() {
            @Override
            public void run() {
                try {
                    mService.stop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void shutdown(Context context) {
        postPendingRunnable(context, new Runnable() {
            @Override
            public void run() {
                try {
                    mService.shutdown();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void registerListener(Context context, final IRecognizerListener listener) {
        postPendingRunnable(context, new Runnable() {
            @Override
            public void run() {
                try {
                    mService.registerListener(mToken, listener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void unregisterListener(Context context) {
        if (mService != null) {
            try {
                mService.unregisterListener(mToken);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopSound(Context context) {
        postPendingRunnable(context, new Runnable() {
            @Override
            public void run() {
                try {
                    mService.stopSound();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public int getState() {
        if (mService != null) {
            try {
                return mService.getState();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }
}
