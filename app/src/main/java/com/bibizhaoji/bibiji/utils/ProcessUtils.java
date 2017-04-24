package com.bibizhaoji.bibiji.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.speech.utils.ContextUtils;

import java.util.List;

/**
 * Created by EdisonChang on 2016/7/2.
 */
public class ProcessUtils {

    private static final String PRODUCT_PROCESS = "com.bibizhaoji.bibiji";
    private static String curProcessName;
    private static final int TRY_GET_PROCESS_NAME_MAX_COUNT = 3;
    private static int getCurProcessNameCount = 0;

    public static String getProcessName(Context context, int pid) {
        String procName = null;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> infos = activityManager.getRunningAppProcesses();
            if (infos != null) {
                for (ActivityManager.RunningAppProcessInfo appProcess : infos) {
                    if (appProcess != null && appProcess.pid == pid) {
                        procName = appProcess.processName;
                        break;
                    }
                }
            }
        }

        return procName;
    }

    public static String getCurProcessName() {
        if (TextUtils.isEmpty(curProcessName)) {
            if (getCurProcessNameCount < TRY_GET_PROCESS_NAME_MAX_COUNT) {
                int pid = android.os.Process.myPid();
                curProcessName = getProcessName(ContextUtils.getApplicationContext(), pid);
                getCurProcessNameCount++;
            }
        }
        return curProcessName;
    }


    public static boolean isMainProcess() {
        return PRODUCT_PROCESS.equalsIgnoreCase(getCurProcessName());
    }
}
