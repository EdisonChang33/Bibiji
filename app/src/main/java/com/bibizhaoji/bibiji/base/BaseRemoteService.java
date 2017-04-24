package com.bibizhaoji.bibiji.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.bibizhaoji.bibiji.utils.PriorityThreadFactory;
import com.speech.utils.LogEnv;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Chenyichang
 */
public abstract class BaseRemoteService {

    private static String TAG = "BaseRemoteService";

    private final static String THREAD_TAG = "bindservice";

    protected abstract void onServiceConnected(ComponentName name, IBinder service);

    protected abstract void onServiceDisconnected(ComponentName name);

    protected abstract boolean isServiceBindSuc();

    protected void afterBindSucess() {
        runPendingTasks();
    }

    protected abstract Intent getBindServiceIntent(Context context);

    private ServiceConnectionImp mServiceConnection = null;

    private final AtomicBoolean isBinding = new AtomicBoolean(false);
    private final List<Runnable> mPendingRunnables = new ArrayList<>();

    private class ServiceConnectionImp implements ServiceConnection {
        public final CountDownLatch mCountDownLatch;
        public BaseRemoteService mServiceClient;

        public ServiceConnectionImp(BaseRemoteService serviceClient) {
            mCountDownLatch = new CountDownLatch(1);
            mServiceClient = serviceClient;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceClient.onServiceConnected(name, service);
            afterBindSucess();
            mCountDownLatch.countDown();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServiceClient.onServiceDisconnected(name);
            mCountDownLatch.countDown();
        }
    }

    // 注意：bindService时用new Intent(getContext(), DaemonCoreService.class)，
    // 而不能使用new Intent("xxx.xxx.ACTION")去启动，“HTC X920e Android 4.2.2”和“红米手机
    // Android 4.2.1”bind能成功，但是onBind方法不执行，原因未明；
    // Intent intent = new Intent(getContext(), DaemonCoreService.class);
    private void bindService(final Context context, final Intent intent) {

        mServiceConnection = new ServiceConnectionImp(this);

        if (!isBinding.getAndSet(true)) {
            PriorityThreadFactory.newThread(THREAD_TAG, new Runnable() {
                @Override
                public void run() {
                    int retryTime = 0;
                    while (retryTime++ <= 10) {
                        if (LogEnv.enable) {
                            Log.d(TAG, "bindService " + retryTime);
                            Log.d("DownloadWatcher", "bindService " + retryTime);
                        }

                        try {
                            if (mServiceConnection == null) {
                                break;
                            }
                            context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
                            try {
                                mServiceConnection.mCountDownLatch.await(1, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                if (LogEnv.enable) {
                                    e.printStackTrace();
                                }
                            } // 1秒
                            if (isServiceBindSuc()) {
                                break;
                            }
                        } catch (IllegalArgumentException e) {

                            if (LogEnv.enable) {
                                e.printStackTrace();
                            }
                        }
                    }
                    isBinding.set(false);
                }
            }).start();
        }
    }

    public void postPendingRunnable(Context context, Runnable r) {
        if (isServiceBindSuc()) {
            r.run();
        } else {
            synchronized (mPendingRunnables) {
                if (isServiceBindSuc()) {
                    r.run();
                } else {
                    bindService(context, getBindServiceIntent(context));
                    mPendingRunnables.add(r);
                }
            }
        }
    }

    public void destroy(Context context) {
        if (isServiceBindSuc()) {
            if (context != null && mServiceConnection != null) {
                try {
                    context.unbindService(mServiceConnection);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                mServiceConnection = null;
            }
        }
    }

    protected void clearRedundantTask() {
        synchronized (mPendingRunnables) {
            mPendingRunnables.clear();
        }
    }

    private void runPendingTasks() {
        synchronized (mPendingRunnables) {
            for (Runnable runnable : mPendingRunnables) {
                runnable.run();
            }
            mPendingRunnables.clear();
        }
    }
}
