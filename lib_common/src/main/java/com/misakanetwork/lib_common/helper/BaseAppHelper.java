package com.misakanetwork.lib_common.helper;

import android.content.Context;

import com.misakanetwork.lib_common.utils.SharedPrefUtils;
import com.misakanetwork.lib_common.utils.VersionHelper;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.helper
 * class name：AppHelper
 * desc：AppHelper
 */
public class BaseAppHelper {

    /**
     * 是否已选择稍后更新
     */
    public static void putLaterUpdate(boolean laterUpdate) {
        SharedPrefUtils.open(SharedPrefUtils.NAME).putBoolean("laterUpdate", laterUpdate);
    }

    /**
     * 获取更新选择状态
     */
    public static boolean getLaterUpdate() {
        return (boolean) SharedPrefUtils.open(SharedPrefUtils.NAME).getBoolean("laterUpdate");
    }

    /**
     * 存储是否第一次进入
     */
    public static void putIsFirstRun() {
        SharedPrefUtils.open(SharedPrefUtils.NAME).putInt("isFirstRun", 1);
    }

    /**
     * 获取是否第一次进入
     */
    public static int getIsFirstRun() {
        return SharedPrefUtils.open(SharedPrefUtils.NAME).getInt("isFirstRun", 0);
    }

    /**
     * 存储版本号
     */
    public static void putVersionName(Context context) {
        SharedPrefUtils.open(SharedPrefUtils.NAME).putString("versionName", String.valueOf(VersionHelper.getVersionName(context)));
    }

    /**
     * 获取版本号
     */
    public static String getVersionName() {
        String versionName = (String) SharedPrefUtils.open(SharedPrefUtils.NAME).getString("versionName");
        versionName = versionName == null ? "" : versionName;
        return versionName;
    }

    /**
     * 获取token
     */
    public static String getToken() {
        String token = (String) SharedPrefUtils.open(SharedPrefUtils.NAME).getString("token");
        token = token == null ? "Basic bWJjbG91ZDptYmNsb3Vk" : token;
        return token;
    }

    /**
     * 保存下载apk
     */
    public static void putApk(String path) {
        SharedPrefUtils.open(SharedPrefUtils.NAME).putString("apk", path);
    }

    /**
     * 获取下载apk
     */
    public static String getApk() {
        String path = (String) SharedPrefUtils.open(SharedPrefUtils.NAME).getString("apk");
        return path == null ? "" : path;
    }
}
