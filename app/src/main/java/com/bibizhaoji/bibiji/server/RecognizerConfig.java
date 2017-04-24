package com.bibizhaoji.bibiji.server;

/**
 * Created by EdisonChang on 2016/6/30.
 */
public class RecognizerConfig {

    public static final String REC_WORD1 = "逼逼机";
    public static final String REC_WORD2 = "你在哪里";
    public static final String[] REC_WORDS = {"逼", "机", "你", "在", "哪", "里"};

    // 识别不出来或为空
    public static final int STATE_NONE = 0;
    // 命中分词
    public static final int STATE_MARK = 1;
    // 识别出关键词
    public static final int STATE_MATCH = 2;
}
