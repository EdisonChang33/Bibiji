package com.bibizhaoji.bibiji.server.support;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.speech.utils.LogEnv;


/**
 * @author jinmeng
 */
public class KLServiceHelper {

    private static final String TAG = "KLServiceHelper";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void scheduleJobs(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        // 周期唤醒
        schedule(KLJobService.PERIODIC_JOB_ID, context);
        // 充电唤醒
        schedule(KLJobService.CHARGING_JOB_ID, context);
        // 设备空闲唤醒
        schedule(KLJobService.IDLE_JOB_ID, context);
        // 不计费网络连接时唤醒
        schedule(KLJobService.UNMETERED_NETWORK_JOB_ID, context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    static void schedule(int jobId, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        if (LogEnv.enable) {
            Log.d(TAG, "scheduleJob(): JobId=" + jobId);
        }

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler == null) {
            // 错误时输出日志
            if (LogEnv.enable) {
                Log.d(TAG, "KLServiceHelper.schedule() jobScheduler is null");
            }
            return;
        }
        int nInterval = KeepAliveManager.getCloudInterval() * 1000;
        JobInfo.Builder builder = new JobInfo.Builder(jobId,
                new ComponentName(context.getPackageName(), KLJobService.class.getName()));
        switch (jobId) {
            case KLJobService.PERIODIC_JOB_ID:
                builder.setPeriodic(nInterval);
                break;
            case KLJobService.CHARGING_JOB_ID:
                builder.setMinimumLatency(nInterval);
                builder.setRequiresCharging(true);
                break;
            case KLJobService.IDLE_JOB_ID:
                builder.setMinimumLatency(nInterval);
                builder.setRequiresDeviceIdle(true);
                break;
            case KLJobService.UNMETERED_NETWORK_JOB_ID:
                builder.setMinimumLatency(nInterval);
                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
                break;
            default:
                break;
        }
        // 支持设备重启
        builder.setPersisted(true);
        int result = jobScheduler.schedule(builder.build());
        if (result <= 0) {
            // 错误时输出日志
            if (LogEnv.enable) {
                Log.d(TAG, "KLServiceHelper.schedule() jobScheduler.schedule result <= 0");
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void enableOrNot(boolean enable, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        try {
            enableOrDisableService(context, enable);
        } catch (Throwable e) {
            if (LogEnv.enable) {
                Log.d(TAG, "KLServiceHelper.enableOrNot() enableOrDisableService error, e=" + e.getMessage());
            }
        }

        if (enable) {
            try {
                KLServiceHelper.scheduleJobs(context);
            } catch (Throwable e) {
                e.printStackTrace();
                if (LogEnv.enable) {
                    Log.d(TAG, "KLServiceHelper.enableOrNot() KLServiceHelper.scheduleJobs error, e=" + e.getMessage());
                }
            }
        } else {
            try {
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                jobScheduler.cancelAll();
            } catch (Throwable e) {
                e.printStackTrace();
                if (LogEnv.enable) {
                    Log.d(TAG, "KLServiceHelper.enableOrNot() jobScheduler.cancelAll error, e=" + e.getMessage());
                }
            }
        }
    }

    private static void enableOrDisableService(Context context, boolean enable) {
        final PackageManager pm = context.getPackageManager();
        final ComponentName compName = new ComponentName(context.getPackageName(), KLJobService.class.getName());
        if (pm != null) {
            pm.setComponentEnabledSetting(compName,
                    enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void init(Context context) {
        enableOrNot(true, context);
    }

    public static void initJobService(Context context) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                PackageManager pm = context.getPackageManager();
                ComponentName cn = new ComponentName(context.getPackageName(), KLJobService.CLS_NAME);
                pm.setComponentEnabledSetting(cn, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            } else {
                KLServiceHelper.init(context.getApplicationContext());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
