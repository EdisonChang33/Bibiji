package com.socialize.share;

import com.umeng.socialize.PlatformConfig;

/**
 * Created by EdisonChang on 2016/7/1.
 */
public class ShareHelper {

    public static void init() {
        //微信 appid appsecret
        //新浪微博 appkey appsecret
        // QQ和Qzone appid appkey
        PlatformConfig.setWeixin("xxxxxxx", "xxxxxxx");
        PlatformConfig.setSinaWeibo("xxxxxxx", "xxxxxxx");
        PlatformConfig.setQQZone("xxxxxxx", "xxxxxxx");

    }

}
