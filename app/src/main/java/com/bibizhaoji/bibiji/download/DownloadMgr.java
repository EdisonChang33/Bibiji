package com.bibizhaoji.bibiji.download;

import android.os.Environment;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by EdisonChang on 2016/7/2.
 */
public class DownloadMgr {

    private static final String privateSdCardDownloadPath = "bibizhaoji";

    public static String DownloadPath(String fileName) {
        return getPrivateSdCardDownloadPath() + "/" + fileName + ".apk";
    }

    public static String getPrivateSdCardDownloadPath() {
        String filePath = getSDCardPath() + "/" + privateSdCardDownloadPath;
        createDir(new File(filePath));
        return filePath;
    }

    public static void createDir(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getSDCardPath() {
        return Environment
                .getExternalStorageDirectory().getPath();
    }


    public static void startDownload(JSONObject jsonObject) {

    }
}
