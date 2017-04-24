package com.bibizhaoji.bibiji.push;

import android.text.TextUtils;

import com.bibizhaoji.bibiji.download.DownloadMgr;
import com.bibizhaoji.bibiji.download.Upgrade;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EdisonChang on 2016/7/2.
 */
public class MsgHandler {

    public static void handleNotification(String custom) {
        if (!TextUtils.isEmpty(custom)) {
            try {
                JSONObject jsonObject = new JSONObject(custom);
                String type = jsonObject.optString(MsgType.TYPE);
                switch (type) {
                    case MsgType.TYPE_UPGRADE:
                        Upgrade.startUpdate(jsonObject);
                        break;
                    case MsgType.TYPE_DOWNLOAD:
                        DownloadMgr.startDownload(jsonObject);
                        break;
                    default:
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public static void handleCustomMessage(String custom) {

    }
}
