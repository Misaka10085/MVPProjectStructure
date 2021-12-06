package com.misakanetwork.lib_common.global;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;

import androidx.multidex.MultiDexApplication;

import com.misakanetwork.lib_common.utils.SharedPrefUtils;

/**
 * Created By：Misaka10085
 * on：2021/4/14
 * package：com.misakanetwork.lib_common.global
 * class name：GlobalApplication
 * desc：GlobalApplication
 */
public class GlobalApplication extends MultiDexApplication {
    @SuppressLint("StaticFieldLeak")
    protected static Context context;
    protected static Handler handler;
    protected static int mainThreadId;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();
        // 兼容7.0访问本地资源
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        // End
        SharedPrefUtils.init(this);
    }

    /**
     * 获取上下文对象
     *
     * @return context
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 获取全局handler
     *
     * @return 全局handler
     */
    public static Handler getHandler() {
        return handler;
    }

    /**
     * 获取主线程id
     *
     * @return 主线程id
     */
    public static int getMainThreadId() {
        return mainThreadId;
    }
}
