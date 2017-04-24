package com.bibizhaoji.bibiji.server.support;

import android.app.Activity;
import android.os.Bundle;

import com.bibizhaoji.bibiji.server.RecognizerService;

/**
 * Created by EdisonChang on 2016/6/30.
 */
// 启动后 调启coreservice  然后退出
public class AliveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecognizerService.startService(this, "AliveActivity");
        finish();
    }
}
