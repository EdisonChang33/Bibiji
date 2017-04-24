package com.bibizhaoji.bibiji.base;

import android.app.Application;
import android.content.Context;

import com.bibizhaoji.bibiji.alarm.Alarm;
import com.bibizhaoji.bibiji.client.RecognizerDelegate;
import com.bibizhaoji.bibiji.push.UmengNotification;
import com.bibizhaoji.bibiji.utils.ChannelUtils;
import com.bibizhaoji.bibiji.server.support.KLServiceHelper;
import com.bibizhaoji.bibiji.utils.ProcessUtils;
import com.socialize.share.ShareHelper;
import com.speech.utils.ContextUtils;
import com.speech.utils.LogEnv;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by hiro on 12/16/15.
 */
public class BibijiApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ContextUtils.init(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
     //   ChannelUtils.init(this);

        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.setDebugMode(LogEnv.enable);
        UmengNotification.setMessageHandler(this);

        ShareHelper.init();

        KLServiceHelper.initJobService(this);
        Alarm.initAlarm(this);

        if (ProcessUtils.isMainProcess()){
            RecognizerDelegate.init(this);
        }
    }
}
