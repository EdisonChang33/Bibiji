package com.bibizhaoji.bibiji;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bibizhaoji.bibiji.base.BaseActivity;
import com.bibizhaoji.bibiji.client.RecognizerDelegate;
import com.bibizhaoji.bibiji.download.Upgrade;
import com.bibizhaoji.bibiji.media.MediaHelper;
import com.bibizhaoji.bibiji.server.RecognizerService;
import com.bibizhaoji.bibiji.support.BackSupport;
import com.bibizhaoji.bibiji.support.StatHelper;
import com.bibizhaoji.bibiji.utils.GlobalConfig;
import com.bibizhaoji.bibiji.utils.GlobalConst;
import com.speech.recognizer.IRecognizerListener;
import com.speech.utils.LogEnv;
import com.speech.utils.ThreadUtils;
import com.speech.utils.ToastUtils;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Button mainSwticher;
    private Button nightModeSwitcher;
    private Button stopButton;

    private ImageView stateGif;
    private ImageView stateText;
    private AnimationDrawable gifAnim;
    private final BackSupport mBackSupport = new BackSupport();

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        setState(GlobalConst.STATE_LISTENING);
        RecognizerDelegate.init(this);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onDestroy() {
        RecognizerDelegate.unInit(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gifAnim != null) {
            gifAnim.start();
        }
    }

    @Override
    public void onPause() {
        if (gifAnim != null) {
            gifAnim.stop();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (mBackSupport.canBeBack()){
            super.onBackPressed();
        }else {
            ToastUtils.showShort(this, getString(R.string.back_support_string));
        }
    }

    private void handleIntent(Intent intent) {
        int status = intent != null ? intent.getIntExtra(GlobalConst.EXTRA_STATUS, GlobalConst.STATE_LISTENING) : GlobalConst.STATE_LISTENING;
        if (GlobalConfig.isMainSwitcherOn(MainActivity.this)) {
            mainSwticher.setBackgroundResource(R.drawable.main_switcher_on);
            if (status == GlobalConst.STATE_ACTIVE) {
                setState(status);
            } else {
                if (RecognizerDelegate.getState() == 1) {
                    setState(GlobalConst.STATE_ACTIVE);
                } else {
                    RecognizerDelegate.start(this);
                    setState(GlobalConst.STATE_LISTENING);
                }
            }
        } else {
            mainSwticher.setBackgroundResource(R.drawable.main_switcher_off);
            setState(GlobalConst.STATE_OFF);
        }
    }

    private void initUI() {
        mainSwticher = (Button) findViewById(R.id.main_switcher);
        nightModeSwitcher = (Button) findViewById(R.id.night_mode_switcher);
        stopButton = (Button) findViewById(R.id.stop_btn);

        stateGif = (ImageView) findViewById(R.id.gif_state);
        stateText = (ImageView) findViewById(R.id.text_state);

        mainSwticher.setOnClickListener(this);
        nightModeSwitcher.setOnClickListener(this);
        stopButton.setOnClickListener(this);

        nightModeSwitcher.setBackgroundResource(GlobalConfig.isNightModeOn(MainActivity.this) ? R.drawable.night_mode_on : R.drawable.night_mode_off);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 主服务开关
            case R.id.main_switcher:
                if (GlobalConfig.isMainSwitcherOn(MainActivity.this)) {
                    if (LogEnv.enable) {
                        Log.d("MainActivity", "STATE_OFF");
                    }

                    setState(GlobalConst.STATE_OFF);
                    GlobalConfig.setMainSwitcher(this, false);
                    v.setBackgroundResource(R.drawable.main_switcher_off);
                    RecognizerDelegate.stopSound(this);
                    RecognizerDelegate.stop(this);
                } else {
                    if (LogEnv.enable) {
                        Log.d("MainActivity", "STATE_ON");
                    }
                    setState(GlobalConst.STATE_LISTENING);
                    GlobalConfig.setMainSwitcher(this, true);
                    v.setBackgroundResource(R.drawable.main_switcher_on);
                    RecognizerDelegate.start(this);
                }
                break;
            // 夜间模式开关
            case R.id.night_mode_switcher:
                if (GlobalConfig.isNightModeOn(MainActivity.this)) {
                    v.setBackgroundResource(R.drawable.night_mode_off);
                    GlobalConfig.setNightMode(this, false);
                } else {
                    v.setBackgroundResource(R.drawable.night_mode_on);
                    GlobalConfig.setNightMode(this, true);
                }
                break;

            case R.id.stop_btn:
                RecognizerDelegate.stopSound(this);
                setState(GlobalConst.STATE_STOP);
                break;
        }
    }

    private void setState(int state) {
        if (gifAnim != null) {
            gifAnim.stop();
        }
        switch (state) {
            case GlobalConst.STATE_OFF:
                stateText.setBackgroundResource(R.drawable.bg_main_off);
                stateGif.setBackgroundResource(R.drawable.state_off);
                stopButton.setVisibility(View.GONE);
                break;
            case GlobalConst.STATE_LISTENING:
                stateText.setBackgroundResource(R.drawable.bg_main_listening);
                stateGif.setBackgroundResource(R.drawable.state_listening);
                break;
            case GlobalConst.STATE_ACTIVE:
                stateText.setBackgroundResource(R.drawable.bg_main_active);
                stateGif.setBackgroundResource(R.drawable.state_active);
                stopButton.setVisibility(View.VISIBLE);
                break;
            case GlobalConst.STATE_STOP:
                stateText.setBackgroundResource(R.drawable.bg_main_stop);
                stateGif.setBackgroundResource(R.drawable.state_stop);
                stopButton.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (GlobalConfig.isMainSwitcherOn(MainActivity.this)) {
                            setState(GlobalConst.STATE_LISTENING);
                            RecognizerDelegate.start(MainActivity.this);
                        } else {
                            setState(GlobalConst.STATE_OFF);
                            RecognizerDelegate.stop(MainActivity.this);
                        }
                    }
                }, GlobalConfig.STOP_ANIM_DURATION);

                break;
            default:
                break;
        }
        gifAnim = (AnimationDrawable) stateGif.getBackground();
        gifAnim.start();
    }

    @Override
    protected String getPageId() {
        return StatHelper.PAGE_MAIN;
    }

    @Override
    protected boolean composeByFragment() {
        return false;
    }
}
