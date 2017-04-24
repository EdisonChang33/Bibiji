package com.bibizhaoji.bibiji.push;

import android.content.Context;
import android.util.Log;

import com.speech.utils.ContextUtils;
import com.speech.utils.LogEnv;
import com.speech.utils.ThreadUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by EdisonChang on 2016/7/2.
 */
public class UmengNotification {

    public static void setMessageHandler(Context context) {
        PushAgent.getInstance(context).enable();
        PushAgent.getInstance(context).setNotificationClickHandler(notificationClickHandler);
        PushAgent.getInstance(context).setMessageHandler(messageHandler);
    }

    /**
     * 该Handler是在BroadcastReceiver中被调用，故
     * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
     */
    private static UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

        @Override
        public void dealWithCustomAction(Context context, UMessage msg) {
            if (LogEnv.enable) {
                Log.d("UmengNotification", "notificationClickHandler msg = " + msg.custom);
            }
            MsgHandler.handleNotification(msg.custom);
        }
    };

    //自定义消息没有通知栏，可以自己弹通知栏
    private static UmengMessageHandler messageHandler = new UmengMessageHandler() {
        @Override
        public void dealWithCustomMessage(final Context context, final UMessage msg) {

            if (LogEnv.enable) {
                Log.d("UmengNotification", "messageHandler msg = " + msg.custom);
            }

            //加拉活逻辑
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 对自定义消息的处理方式，点击或者忽略
                    boolean isClickOrDismissed = true;
                    if (isClickOrDismissed) {
                        //自定义消息的点击统计
                        UTrack.getInstance(ContextUtils.getApplicationContext()).trackMsgClick(msg);
                    } else {
                        //自定义消息的忽略统计
                        UTrack.getInstance(ContextUtils.getApplicationContext()).trackMsgDismissed(msg);
                    }

                    MsgHandler.handleCustomMessage(msg.custom);
                }
            });
        }
    };
}
