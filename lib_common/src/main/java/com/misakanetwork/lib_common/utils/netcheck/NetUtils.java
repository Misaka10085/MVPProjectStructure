package com.misakanetwork.lib_common.utils.netcheck;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.misakanetwork.lib_common.utils.L;

import java.io.IOException;

/**
 * Created By：Misaka10085
 * on：2021/7/7
 * package：com.misakanetwork.lib_common.utils
 * class name：NetUtils
 * desc：获取并判断网络状态
 */
public class NetUtils {
    private static final int NETWORK_NONE = -1; // 没有连网
    private static final int NETWORK_MOBILE = 0; // 移动网络
    private static final int NETWORK_WIFI = 1; // 无线网络

    public static int getNetWorkState(Context context) {

        // 获取连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取额外的网络信息
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        // 判断网络处于连接状态
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            // 获取网络的类型
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                L.e("通知", "当前网络处于WiFi状态");
                // 是否可上网
                if (isNetworkOnline()) {
                    return NETWORK_WIFI;
                } else {
                    return NETWORK_NONE;
                }
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }

    //判断当前网络是否能连同外网
    public static boolean isNetworkOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("ping -c 3 www.baidu.com");
            int exitValue = ipProcess.waitFor();
            L.e("Avalible", "Process:" + exitValue);
            // wifi不可用或未连接，返回2；WiFi需要认证，返回1；WiFi可用，返回0；
            return (exitValue == 0);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
