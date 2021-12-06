package com.misakanetwork.mvpprojectstructure.helper;

import com.misakanetwork.lib_common.utils.SharedPrefUtils;
import com.misakanetwork.mvpprojectstructure.bean.ApiSetBean;
import com.misakanetwork.mvpprojectstructure.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By：Misaka10085
 * on：2021/4/14
 * package：com.misakanetwork.mvpprojectstructure.helper
 * class name：AppHelper
 * desc：SharedPreferences helper
 */
public class AppHelper {

    /**
     * 存储当前所有切换的domain
     */
    public static void putDomain(List<ApiSetBean> apiSetBeanList) {
        SharedPrefUtils.open(SharedPrefUtils.NAME).putDataList("currentApiList", apiSetBeanList);
    }

    /**
     * 获取当前所有切换的domain
     */
    public static List<ApiSetBean> getDomain() {
        List<ApiSetBean> apiSetBeanList = SharedPrefUtils.open(SharedPrefUtils.NAME).getDataList("currentApiList", ApiSetBean.class);
        apiSetBeanList = apiSetBeanList == null ? new ArrayList<>() : apiSetBeanList;
        return apiSetBeanList;
    }

    /**
     * 储存Token
     */
    public static void putToken(String token) {
        SharedPrefUtils.open(SharedPrefUtils.NAME).putString("token", token);
        UserBean userBean = getUser();
        userBean.setAccess_token(token);
        putUser(userBean);
    }

    public static String getToken() {
        String token = (String) SharedPrefUtils.open(SharedPrefUtils.NAME).getString("token");
        return token == null ? "" : token;
    }

    public static void removeToken() {
        SharedPrefUtils.open(SharedPrefUtils.NAME).remove("token");
    }

    /**
     * 储存用户
     */
    public static void putUser(UserBean userBean) {
        SharedPrefUtils.open(SharedPrefUtils.NAME).putEntity("user", userBean);
    }

    public static void removeUser() {
        SharedPrefUtils.open(SharedPrefUtils.NAME).remove("user");
    }

    /**
     * 取本地用户
     */
    public static UserBean getUser() {
        UserBean userBean = (UserBean) SharedPrefUtils.open(SharedPrefUtils.NAME).getEntity("user", UserBean.class);
        return userBean == null ? new UserBean() : userBean;
    }

    /**
     * 移除所有缓存信息
     */
    public static void removeAll() {
        removeToken();
        removeUser();
    }

    /**
     * 存储是否第一次进入需要跳转引导页
     */
    public static void putIsFirstGuide() {
        SharedPrefUtils.open(SharedPrefUtils.NAME).putInt("isFirstRunGuide", 1);
    }

    /**
     * 获取是否第一次进入需要跳转引导页
     */
    public static int getIsFirstGuide() {
        return SharedPrefUtils.open(SharedPrefUtils.NAME).getInt("isFirstRunGuide", 0);
    }

    /**
     * 存储夜间模式状态
     */
    public static void putIsNightMode(boolean isNightMode) {
        SharedPrefUtils.open(SharedPrefUtils.NAME).putBoolean("isNightMode", isNightMode);
    }

    /**
     * 获取夜间模式状态
     */
    public static boolean getIsNightMode() {
        return (boolean) SharedPrefUtils.open(SharedPrefUtils.NAME).getBoolean("isNightMode");
    }
}
