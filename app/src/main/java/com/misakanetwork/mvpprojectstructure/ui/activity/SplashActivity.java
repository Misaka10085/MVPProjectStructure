package com.misakanetwork.mvpprojectstructure.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.helper.AppHelper;
import com.misakanetwork.mvpprojectstructure.ui.activity.base.BaseCenterActivity;

import butterknife.BindView;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.ui.activity
 * class name：SplashActivity
 * desc：SplashActivity
 */
public class SplashActivity extends BaseCenterActivity {
    @BindView(R.id.splash_bg)
    ImageView splashBg;

    public static void startThis(Context context) {
        context.startActivity(new Intent(context.getApplicationContext(), SplashActivity.class));
    }

    private Handler handler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected boolean isImmerse() {
        return true;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        registerRxBus();
        setTextWhite();
        jumpPost();
    }

    private void jumpPost() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                jump();
            }
        }, 3000);
    }

    private void jump() {
        int isFirst = AppHelper.getIsFirstGuide();
        if (isFirst == 0) {
            GuideActivity.startThis(SplashActivity.this);
            AppHelper.putIsFirstGuide();
        } else {
            MainTestActivity.startThis(SplashActivity.this);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
