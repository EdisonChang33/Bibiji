package com.bibizhaoji.bibiji.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.speech.utils.ContextUtils;


public class NetUtils {


    public static NetworkInfo getCurNetInfo(boolean realTime) {
        return NetworkMonitor.getNetworkInfo(realTime);
    }

    public static NetworkInfo getCurNetInfoByType(int type) {
        return ((ConnectivityManager) ContextUtils.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(type);
    }


    public static boolean isNetworkConnected() {
        NetworkInfo netWorkInfo = NetUtils.getCurNetInfo(false);
        return netWorkInfo != null && netWorkInfo.isConnected();
    }


    public static boolean isDataNetwork() {
        NetworkInfo networkInfo = NetUtils.getCurNetInfo(false);
        boolean bRet = networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected();
        if (!bRet) { // 湖南一个移动3G网络用户，他的 netType 是 50 。
            NetworkInfo mobileNetworkInfo = NetUtils.getCurNetInfoByType(ConnectivityManager.TYPE_MOBILE);
            if (mobileNetworkInfo != null && NetUtils.getCurNetInfo(true) == mobileNetworkInfo) {
                if (mobileNetworkInfo.isConnected()) {
                    bRet = true;
                }
            }
        }
        return bRet;
    }
}
