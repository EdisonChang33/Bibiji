package com.bibizhaoji.bibiji.utils;

import android.content.Context;
import android.util.Log;

import com.bibizhaoji.bibiji.R;
import com.speech.utils.LogEnv;

import java.util.Calendar;

public class GlobalConfig {

    public static final String LOG_TAG = "BiBiJi";
    public static final String LOG_TAG_CONNECTION = "BiBiJi_connection";
    public static final String LOG_TAG_RECWORD = "BiBiJi_recword";

    private static String BIBIJI_PREF = "BiBiJi_pref";
    private static String MAIN_SWITCHER = "main_switcher";
    private static String NIGHT_MODE = "NIGHT_MODE";

    public static final int RINGTON = R.raw.rington;
    public static final float VOLUME = 1.0F;

    public static boolean isMainActivityRunning;
    public static boolean isWorkTime;
    public static boolean isScreenOff;

    public static int STOP_ANIM_DURATION = 2400;

    private static int[] mStartTime = {0, 0};// 默认00:00
    private static int[] mEndTime = {7, 0};// 默认07:00


    public static final void setMainSwitcher(Context context, boolean enable) {
        SPUtils.put(BIBIJI_PREF, context, MAIN_SWITCHER, enable);
    }

    public static final void setNightMode(Context context, boolean enable) {
        SPUtils.put(BIBIJI_PREF, context, NIGHT_MODE, enable);
    }

    public static final boolean isMainSwitcherOn(Context context) {
        return (boolean) SPUtils.get(BIBIJI_PREF, context, MAIN_SWITCHER, true);
    }

    public static final boolean isNightModeOn(Context context) {
        return (boolean) SPUtils.get(BIBIJI_PREF, context, NIGHT_MODE, true);
    }

    /**
     * 判断是否是当天工作时段
     *
     * @return
     */
    public static boolean isWorkingTime(Context context) {

        if (isNightModeOn(context)) {
            // 开启夜间免打扰模式，在夜间(00:00-07:00)server不允许运行
            Calendar cal = Calendar.getInstance();// 当前日期
            int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
            int minute = cal.get(Calendar.MINUTE);// 获取分钟
            int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
            final int start = mStartTime[0] * 60 + mStartTime[1];// 起始时间
            // 00:00的分钟数
            final int end = mEndTime[0] * 60 + mEndTime[1];// 结束时间 07:00的分钟数

            if (minuteOfDay >= start && minuteOfDay <= end) {
                if (LogEnv.enable) {
                    Log.d(LOG_TAG, "在时间范围内");
                }
                return false;
            } else {
                if (LogEnv.enable) {
                    Log.d(LOG_TAG, "在时间范围外");
                }
                return true;
            }
        } else {
            // 没开启免打扰模式，默认server可以运行
            return true;
        }
    }
}
