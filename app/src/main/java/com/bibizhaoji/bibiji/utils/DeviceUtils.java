package com.bibizhaoji.bibiji.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by EdisonChang on 2016/7/2.
 */
public class DeviceUtils {

    private static final int VERSION_CODE = 2;
    private static final String VERSION_NAME = "2.0";

    public static String getVersionName(Context context, String pkg) {
        String versionName;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(pkg, 0);
            versionName = String.format("%s", pi.versionName);
            if (versionName != null && versionName.length() != 0) {
                return versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return VERSION_NAME;
    }

    public static int getVersionCode(Context context, String pkg) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(pkg, 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return VERSION_CODE;
    }

    public static String getVersionName(Context context) {
        return getVersionName(context, context.getPackageName());
    }

    public static int getVersionCode(Context context) {
        return getVersionCode(context, context.getPackageName());
    }
}
