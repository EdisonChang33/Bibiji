package com.bibizhaoji.bibiji.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.speech.utils.ContextUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jinmeng 全局网络变化监听
 */
public class NetworkMonitor {

    private final static String TAG = "NetworkMonitor";

    private boolean mIsNetWorkConnect = false;
    private int mNetType;
    private static NetworkInfo curNetworkInfo = NetUtils.getCurNetInfo(true);

    private NetworkMonitor() {
    }

    private static class InstanceHolder {
        static final NetworkMonitor instance = new NetworkMonitor();
    }

    public static NetworkMonitor getGlobalNetworkMonitor() {
        return InstanceHolder.instance;
    }

    private final Map<NetworkMonitorObserver, String> mObservers = new ConcurrentHashMap<NetworkMonitorObserver, String>();

    public interface NetworkMonitorObserver {
        void onNetworkStatusChanged(boolean isConnected);
    }

    public void addNetworkMonitorObserver(NetworkMonitorObserver observer) {
        mObservers.put(observer, TAG);
        NetworkInfo networkInfo = NetUtils.getCurNetInfo(true);
        if (networkInfo != null) {
            observer.onNetworkStatusChanged(networkInfo.isConnected());
        }
    }

    public void removeNetworkMonitorObserver(NetworkMonitorObserver observer) {
        mObservers.remove(observer);
    }

    public void notifyObserverNetworkChanged(NetworkInfo newNetworkInfo) {

        if (newNetworkInfo == null) {
            mIsNetWorkConnect = false;
            mNetType = -1;
            return;
        } else {
            curNetworkInfo = newNetworkInfo;
            boolean isNetWorkConnect = newNetworkInfo.isConnected();
            int netType = newNetworkInfo.getType();

            if (mIsNetWorkConnect != isNetWorkConnect || netType != mNetType) {
                for (NetworkMonitorObserver observer : mObservers.keySet()) {
                    observer.onNetworkStatusChanged(isNetWorkConnect);
                }
            }
            mIsNetWorkConnect = isNetWorkConnect;
            mNetType = netType;
        }
    }

    public static NetworkInfo getNetworkInfo(boolean realTime) {
        if (curNetworkInfo == null || realTime) {
            curNetworkInfo = ((ConnectivityManager) ContextUtils.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        }
        return curNetworkInfo;
    }
}
