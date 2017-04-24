package com.bibizhaoji.bibiji.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bibizhaoji.bibiji.client.RecognizerDelegate;
import com.bibizhaoji.bibiji.utils.GlobalConfig;
import com.speech.utils.LogEnv;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String actionString = intent.getAction();
        if (intent.getAction().equals(Alarm.WAKEUP_ALARM_ACTION)) {
            // 早上七点
            GlobalConfig.isWorkTime = true;
            Alarm.initAlarm(context);

            if (LogEnv.enable) {
                Log.d("AlarmReceiver", "AlarmReceiver wake");
            }

            if (GlobalConfig.isMainSwitcherOn(context)) {
                RecognizerDelegate.start(context);
            }
        } else {
            GlobalConfig.isWorkTime = false;
            if (LogEnv.enable) {
                Log.d("AlarmReceiver", "AlarmReceiver sleep");
            }

            RecognizerDelegate.shutdown(context);
        }

        if (LogEnv.enable) {
            Log.d(GlobalConfig.LOG_TAG, actionString + "闹钟时间到");
        }
    }
}
