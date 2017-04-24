package com.bibizhaoji.bibiji.alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bibizhaoji.bibiji.utils.GlobalConfig;
import com.speech.utils.LogEnv;

public class Alarm {

    public static final String ALARM_NAME = "ALARM_NAME";
    public static final String SLEEP_ALARM_ACTION = "sleep_alarm";
    public static final String WAKEUP_ALARM_ACTION = "wakeup_alarm";

    public static void initAlarm(Context context) {
        setAlarm(context, sleepAlarm(), SLEEP_ALARM_ACTION);
        setAlarm(context, wakeUpAlarm(), WAKEUP_ALARM_ACTION);
    }

    private static Calendar wakeUpAlarm() {
        if (LogEnv.enable) {
            Log.d(GlobalConfig.LOG_TAG, "wakeUpAlarm");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 1); // 将日期加一
        // calendar.add(Calendar.SECOND, 10);
        return calendar;
    }

    private static void setAlarm(Context context, Calendar calendar,
                                 String alarmActionName) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(alarmActionName);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);// 设置闹钟
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), (24 * 60 * 60 * 1000), pi);// 重复设置

        if (LogEnv.enable) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
            String time = format.format(calendar.getTime());
            Log.d(GlobalConfig.LOG_TAG, "闹钟日期-->" + time);
        }
    }

    private static Calendar sleepAlarm() {

        if (LogEnv.enable) {
            Log.d(GlobalConfig.LOG_TAG, "sleepAlarm");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 1); // 将日期加一
        // calendar.add(Calendar.SECOND, 15);

        return calendar;
    }
}
