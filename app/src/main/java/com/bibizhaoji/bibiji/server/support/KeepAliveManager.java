package com.bibizhaoji.bibiji.server.support;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.bibizhaoji.bibiji.server.RecognizerService;

/**
 * Created by EdisonChang on 2016/6/30.
 */
public class KeepAliveManager {

    private static final int DEFAULT_KEEP_ALIVE_INTERVAL = 60 * 10;

    public static void startByAlarmManager(RecognizerService service) {
        Intent intent = new Intent(service.getApplicationContext(), service.getClass());
        intent.setPackage(service.getPackageName());
        PendingIntent pendingIntent = PendingIntent.getService(service.getApplicationContext(), 1, intent, PendingIntent.FLAG_ONE_SHOT);

        // 5秒后启动
        AlarmManager alarmService = (AlarmManager) service.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 5000, pendingIntent);
    }

    public static int getCloudInterval() {
        return DEFAULT_KEEP_ALIVE_INTERVAL;
    }
}
