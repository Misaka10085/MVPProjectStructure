package com.misakanetwork.mvpprojectstructure.ui.activity.base;

import android.graphics.Color;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.mvpprojectstructure.ui.base.activity
 * class name：BaseCenterActivity
 * desc：BaseCenterActivity , 不需要网络请求可继承该Activity
 */
public abstract class BaseCenterActivity extends BaseActivity {
    /**
     * 默认不沉浸
     */
    @Override
    protected boolean isImmerse() {
        return false;
    }

    @Override
    protected int statusColor() {
        return Color.BLACK;
    }

    @Override
    protected int statusColorBy23() {
        return Color.WHITE;
    }
}
