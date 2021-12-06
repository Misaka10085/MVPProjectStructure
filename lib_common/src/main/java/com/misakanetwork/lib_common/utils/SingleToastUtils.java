package com.misakanetwork.lib_common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.misakanetwork.lib_common.R;

import me.jessyan.autosize.AutoSize;

/**
 * Created By：Misaka10085
 * on：2021/6/18
 * package：com.misakanetwork.lib_common.utils
 * class name：SingleToastUtils
 * desc：SingleToastUtils
 */
@SuppressLint({"InflateParams", "StaticFieldLeak"})
public class SingleToastUtils {
    private static SingleToastUtils mInstance = null;
    private Toast mToast;
    private TextView mToastView;
    private static Context mContext;

    private SingleToastUtils(Context context) {
        mContext = context;
    }

    public static SingleToastUtils init(Context context) {
        synchronized (SingleToastUtils.class) {
            if (mInstance == null) {
                synchronized (SingleToastUtils.class) {
                    if (mInstance == null) {
                        mInstance = new SingleToastUtils(context);
                    }
                }
            }
            return mInstance;
        }
    }

    public void showNormalApp(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (mToast == null) {
            View layout = LayoutInflater.from(mContext).inflate(R.layout.toast_layout, null);
            mToastView = (TextView) layout.findViewById(R.id.message_tv);
            mToast = new Toast(mContext);
            mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setView(layout);
        }
        mToastView.setText(msg);
        mToast.show();
    }

    public void showNormal(String msg) {
        AutoSize.autoConvertDensityOfGlobal((Activity) mContext);
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (mToast == null) {
            View layout = LayoutInflater.from(mContext).inflate(R.layout.toast_layout, null);
            mToastView = (TextView) layout.findViewById(R.id.message_tv);
            mToast = new Toast(mContext);
            mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 50);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setView(layout);
        }
        mToastView.setText(msg);
        mToast.show();
    }

    public void showLong(String msg) {
        AutoSize.autoConvertDensityOfGlobal((Activity) mContext);
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (mToast == null) {
            View layout = LayoutInflater.from(mContext).inflate(R.layout.toast_layout, null);
            mToastView = (TextView) layout.findViewById(R.id.message_tv);
            mToast = new Toast(mContext);
            mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 50);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setView(layout);
        }
        mToastView.setText(msg);
        mToast.show();
    }

    public void showLongApp(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (mToast == null) {
            View layout = LayoutInflater.from(mContext).inflate(R.layout.toast_layout, null);
            mToastView = (TextView) layout.findViewById(R.id.message_tv);
            mToast = new Toast(mContext);
            mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 50);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setView(layout);
        }
        mToastView.setText(msg);
        mToast.show();
    }
}
