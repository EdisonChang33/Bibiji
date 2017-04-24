package com.bibizhaoji.bibiji.server.support;

import android.annotation.TargetApi;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.bibizhaoji.bibiji.server.RecognizerService;
import com.speech.utils.LogEnv;

/**
 * Created by EdisonChang on 2016/6/30.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotifyListenerService extends NotificationListenerService {

    private static final String TAG = "NotifyListenerService";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if (LogEnv.enable) {
            Log.d(TAG, "NotifyListenerService onNotificationPosted");
        }
        RecognizerService.startService(this, TAG);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        if (LogEnv.enable) {
            Log.d(TAG, "NotifyListenerService onNotificationRemoved");
        }
        RecognizerService.startService(this, TAG);
    }
}
