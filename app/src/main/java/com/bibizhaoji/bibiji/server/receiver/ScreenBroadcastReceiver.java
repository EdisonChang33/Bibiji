package com.bibizhaoji.bibiji.server.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bibizhaoji.bibiji.server.RecognizerService;
import com.speech.utils.LogEnv;

public class ScreenBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "ScreenReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        if (LogEnv.enable) {
            String action = intent == null ? null : intent.getAction();
            Log.d(TAG, "onReceive == " + action);
        }

        RecognizerService.startService(context, TAG);
    }

}
