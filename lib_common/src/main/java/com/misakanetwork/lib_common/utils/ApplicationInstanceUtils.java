package com.misakanetwork.lib_common.utils;

import android.content.Context;

import com.misakanetwork.lib_common.global.GlobalApplication;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.utils
 * class name：ApplicationInstanceUtils
 * desc：ApplicationInstanceUtils
 */
public class ApplicationInstanceUtils {
    private final Context context = GlobalApplication.getContext();

    private static ApplicationInstanceUtils instance = null;

    public static ApplicationInstanceUtils getInstance() {
        if (instance == null) {
            synchronized (ApplicationInstanceUtils.class) {
                if (instance == null) {
                    instance = new ApplicationInstanceUtils();
                }
            }
        }
        return instance;
    }

    private ApplicationInstanceUtils() {
    }

    public Context getContext() {
        return context;
    }
}
