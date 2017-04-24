package com.bibizhaoji.bibiji.server.support;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import com.bibizhaoji.bibiji.server.RecognizerService;
import com.speech.utils.LogEnv;

/**
 * @author jinmeng
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class KLJobService extends JobService {

    public static final String CLS_NAME = "com.bibizhaoji.bibiji.server.support.KLJobService";

    public static final int PERIODIC_JOB_ID = 0x01;
    public static final int CHARGING_JOB_ID = 0x02;
    public static final int IDLE_JOB_ID = 0x03;
    public static final int UNMETERED_NETWORK_JOB_ID = 0x04;

    private static final String TAG = "KLJobService";

    public void onStartJobImpl(int jobId) {
        switch (jobId) {
            case PERIODIC_JOB_ID:
                break;
            default:
                KLServiceHelper.schedule(jobId, getApplicationContext());
                break;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LogEnv.enable) {
            Log.d(TAG, "KLJobService -- onCreate");
        }

        RecognizerService.startService(this, "KLJobService");
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        if (LogEnv.enable) {
            Log.d(TAG, "---KLJobService onStartJob---");
        }

        try {
            onStartJobImpl(params.getJobId());
        } catch (Throwable e) {
            if (LogEnv.enable) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
