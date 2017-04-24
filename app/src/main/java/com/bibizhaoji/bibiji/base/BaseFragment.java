package com.bibizhaoji.bibiji.base;

import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by hiro on 12/16/15.
 */
public abstract class BaseFragment extends Fragment {

    protected abstract String getPageId();

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getPageId());
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getPageId());
    }

}
