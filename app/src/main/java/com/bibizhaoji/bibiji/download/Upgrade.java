package com.bibizhaoji.bibiji.download;

import android.text.TextUtils;

import com.bibizhaoji.bibiji.R;
import com.bibizhaoji.bibiji.support.OkHttpClientManager;
import com.bibizhaoji.bibiji.utils.DeviceUtils;
import com.speech.utils.ContextUtils;
import com.speech.utils.ToastUtils;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by EdisonChang on 2016/7/2.
 */
public class Upgrade {

    private static final String APP_INFO_URL = "http://openbox.mobilem.360.cn/iservice/getAppDetail?market_id=360market&pname=com.bibizhaoji.bibiji";

    public static void checkUpdate() {
        OkHttpClientManager.getAsyn(APP_INFO_URL, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                ToastUtils.showShort(ContextUtils.getApplicationContext(), ContextUtils.getApplicationContext().getString(R.string.check_update_fail));
            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String errno = jsonObject.optString("errno", "0");
                        JSONArray data = jsonObject.optJSONArray("data");
                        if ("0".equals(errno) && data != null && data.length() > 0) {
                            startUpdate(data.getJSONObject(0));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void startUpdate(JSONObject jsonObject) {
        ApkInfo apkInfo = new ApkInfo();
        if (apkInfo.parse(jsonObject)) {
            if (!TextUtils.isEmpty(apkInfo.versionCode)) {
                int webVer = Integer.parseInt(apkInfo.versionCode);
                if (webVer > DeviceUtils.getVersionCode(ContextUtils.getApplicationContext())) {
                    if (!TextUtils.isEmpty(apkInfo.downloadUrl)) {
                        OkHttpClientManager.downloadAsyn(apkInfo.downloadUrl,
                                DownloadMgr.getPrivateSdCardDownloadPath(),
                                new OkHttpClientManager.ResultCallback<String>() {
                                    @Override
                                    public void onError(Request request, Exception e) {
                                        ToastUtils.showShort(ContextUtils.getApplicationContext(), ContextUtils.getApplicationContext().getString(R.string.download_fail));
                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        if (!TextUtils.isEmpty(response)) {
                                            Installer.install(ContextUtils.getApplicationContext(), response);
                                        }
                                    }
                                });
                    }
                } else {
                    ToastUtils.showShort(ContextUtils.getApplicationContext(), ContextUtils.getApplicationContext().getString(R.string.check_update_new));
                }
            }
        }
    }

    public static class ApkInfo {

        public String serverId;
        public String resPackageName;
        public String resName;
        public String downloadUrl;

        public String versionCode; // 服务器 端的版本号
        public String versionName;
        public String apkSignatureMd5; // 签名的md5

        public String resSize;
        public String resMd5;

        public String logoUrl;

        public boolean parse(JSONObject jsonObject) {
            if (jsonObject != null) {
                serverId = jsonObject.optString("id");
                resPackageName = jsonObject.optString("apkid");
                resName = jsonObject.optString("name");
                downloadUrl = jsonObject.optString("down_url");
                versionCode = jsonObject.optString("version_code");
                versionName = jsonObject.optString("version_name");
                apkSignatureMd5 = jsonObject.optString("signature_md5");

                resSize = jsonObject.optString("size");
                resMd5 = jsonObject.optString("apk_md5");

                logoUrl = jsonObject.optString("logo_url");
                return true;
            }

            return false;
        }
    }
}
