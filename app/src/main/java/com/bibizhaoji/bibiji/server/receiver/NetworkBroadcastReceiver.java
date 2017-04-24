package com.bibizhaoji.bibiji.server.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.bibizhaoji.bibiji.server.RecognizerService;
import com.speech.utils.LogEnv;
import com.bibizhaoji.bibiji.utils.NetUtils;
import com.bibizhaoji.bibiji.utils.NetworkMonitor;


public class NetworkBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (LogEnv.enable){
            String action = intent == null ? null : intent.getAction();
            Log.d("NetworkReceiver", "onReceive == " + action);
        }

        RecognizerService.startService(context, "NetworkReceiver");

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkMonitor.getGlobalNetworkMonitor().notifyObserverNetworkChanged(NetUtils.getCurNetInfo(true));
        }
    }
}