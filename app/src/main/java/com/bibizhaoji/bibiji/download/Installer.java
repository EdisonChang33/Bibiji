package com.bibizhaoji.bibiji.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by EdisonChang on 2016/7/2.
 */
public class Installer {

    public static final String APK_MIMETPYE_PREFIX = "application/vnd.android.package-archive";

    public static void install(Context context, String apkFilePath) {

        File targetFile = new File(apkFilePath);
        if (targetFile != null && targetFile.exists()) {
            Intent localIntent = new Intent("android.intent.action.VIEW");
            Uri localUri = Uri.parse("file://" + apkFilePath);
            localIntent
                    .setDataAndType(localUri, APK_MIMETPYE_PREFIX);
            //三星N9150 调系统安装会出现闪屏页 加个FLAG_ACTIVITY_NO_ANIMATION标记
            localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(localIntent);
        }

    }

    public static void uninstall(Context mContext, String packageName) {
        Intent intent;
        Uri uri;
        try {
            uri = Uri.fromParts("package", packageName, null);
            intent = new Intent(Intent.ACTION_DELETE, uri);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
