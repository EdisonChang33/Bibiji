package com.bibizhaoji.bibiji.utils;

import android.content.Context;

import com.umeng.analytics.AnalyticsConfig;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by hiro on 12/16/15.
 */
public class ChannelUtils {

    public final static String CHANNEL_DEFAULT = "self";
    private final static String FILENAME_CHANNEL = "channel";

    private static String channelKey = null;

    public static void init(Context context) {
        try {
            Scanner scanner = new Scanner(context.getAssets().open(FILENAME_CHANNEL));
            String channel = scanner.nextLine().trim();
            if (channel.length() > 0) {
                channelKey = channel.toLowerCase();
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channelKey == null) {
                channelKey = CHANNEL_DEFAULT;
            }
            // AnalyticsConfig.setChannel(channelKey);
        }
    }

    public static String getChannel() {
        return channelKey != null ? channelKey : CHANNEL_DEFAULT;
    }

}
