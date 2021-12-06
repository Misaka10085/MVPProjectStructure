package com.misakanetwork.mvpprojectstructure.utils;

import android.text.TextUtils;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.HttpUrl;

import static com.misakanetwork.lib_common.apis.Api.AREA_FILE_DEFAULT_NAME;

/**
 * Created By：Misaka10085
 * on：2021/6/18
 * package：com.misakanetwork.mvpprojectstructure.utils
 * class name：NetPathFetchUtils
 * desc：网络地址拼接
 */
public class NetPathFetchUtils {

    public static String getHttpPath(String oldPath) {
        if (!oldPath.contains("http://") && !oldPath.contains("https://")) {
            oldPath = "http://" + oldPath;
        }
        return oldPath;
    }

    /**
     * 判断图片地址是否是否是全路径，如果是就直接返回，否则补全返回
     */
    public static String getImgPath(String oldPath) {
        HttpUrl httpUrl = RetrofitUrlManager.getInstance().fetchDomain(AREA_FILE_DEFAULT_NAME);
        String basePath = "";
        if (oldPath == null || TextUtils.isEmpty(oldPath)) {
            return "";
        }
        if (oldPath.contains("http:") || oldPath.contains("https:")) {
            return oldPath;
        } else if (httpUrl != null) {
            basePath = httpUrl.toString();
        }
        return basePath + oldPath;
    }

    /**
     * 判断资源路径是否是全路径，不是补全返回
     */
    public static String getResUrlPath(String oldPath) {
        HttpUrl httpUrl = RetrofitUrlManager.getInstance().fetchDomain(AREA_FILE_DEFAULT_NAME);
        String basePath = "";
        if (httpUrl != null) {
            basePath = httpUrl.toString();
        }
        if (oldPath == null || TextUtils.isEmpty(oldPath)) {
            return "";
        }
        if (!oldPath.contains("http")) {
            oldPath = basePath + oldPath;
        }
        return oldPath;
    }
}
