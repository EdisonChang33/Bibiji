package com.bibizhaoji.bibiji;

import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.bibizhaoji.bibiji.base.BaseActivity;
import com.speech.utils.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by EdisonChang on 2016/7/1.
 */
public class ShareProxyActivity extends BaseActivity {

    private UMShareAPI mShareAPI;
    private UMImage image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();

        setContentView(R.layout.activity_share);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });
        mShareAPI = UMShareAPI.get(this);
        doOauthVerify();

        image = new UMImage(ShareProxyActivity.this,
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
    }

    @Override
    protected String getPageId() {
        return null;
    }

    @Override
    protected boolean composeByFragment() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    private void doOauthVerify() {
        mShareAPI.doOauthVerify(this, SHARE_MEDIA.SINA, umAuthListener);
        mShareAPI.doOauthVerify(this, SHARE_MEDIA.WEIXIN, umAuthListener);
        mShareAPI.doOauthVerify(this, SHARE_MEDIA.QQ, umAuthListener);
    }

    private void requestPermissions() {

        //可以将一下代码加到你的MainActivity中，或者在任意一个需要调用分享功能的activity当中
        String[] mPermissionList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_LOGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.SET_DEBUG_APP,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.GET_ACCOUNTS};

        ActivityCompat.requestPermissions(ShareProxyActivity.this, mPermissionList, 100);
    }

    public void share() {

        List<SHARE_MEDIA> medias = new ArrayList<>();

        if (mShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)) {
            medias.add(SHARE_MEDIA.WEIXIN);
            medias.add(SHARE_MEDIA.WEIXIN_CIRCLE);
        }

        if (mShareAPI.isInstall(this, SHARE_MEDIA.SINA)) {
            medias.add(SHARE_MEDIA.SINA);
        }

        if (mShareAPI.isInstall(this, SHARE_MEDIA.QQ)) {
            medias.add(SHARE_MEDIA.QQ);
            medias.add(SHARE_MEDIA.QZONE);
        }

        if (medias.isEmpty()) {
            ToastUtils.showShort(this, "请先安装分享工具！");
        } else {
            SHARE_MEDIA[] displaylist = (SHARE_MEDIA[]) medias.toArray();
            new ShareAction(this).setDisplayList(displaylist)
                    .withText("呵呵")
                    .withTitle("title")
                    .withTargetUrl("http://www.baidu.com")
                    .withMedia(image)
                    .setListenerList(umShareListener)
                    .open();
        }
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtils.showShort(ShareProxyActivity.this, platform + " 分享成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.showShort(ShareProxyActivity.this, platform + " 分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showShort(ShareProxyActivity.this, platform + " 分享取消了");
        }
    };

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            ToastUtils.showShort(ShareProxyActivity.this, platform + " Authorize succeed");
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            ToastUtils.showShort(ShareProxyActivity.this, platform + " Authorize fail");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            ToastUtils.showShort(ShareProxyActivity.this, platform + " Authorize cancel");
        }
    };
}
