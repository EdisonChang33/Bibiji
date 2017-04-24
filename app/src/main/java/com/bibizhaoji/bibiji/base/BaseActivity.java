package com.bibizhaoji.bibiji.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

/**
 * Created by hiro on 12/16/15.
 */
public abstract class BaseActivity extends FragmentActivity {

    protected abstract String getPageId();

    protected abstract boolean composeByFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 此方法与统计分析sdk中统计日活的方法无关！请务必调用此方法！*/
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!composeByFragment()) {
            MobclickAgent.onPageStart(getPageId());
        }

        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        if (!composeByFragment()) {
            MobclickAgent.onPageEnd(getPageId());
        }

        MobclickAgent.onPause(this);
        super.onPause();
    }
}
