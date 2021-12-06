package com.misakanetwork.lib_common.utils.screen;

import android.content.Context;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.utils.screen
 * class name：OppoNotchUtils
 * desc：Oppo刘海屏适配工具类
 */
public class OppoNotchUtils {

    /**
     * 判断是否有刘海屏
     *
     * @param context
     * @return true：有刘海屏；false：没有刘海屏
     */
    public static boolean hasNotch(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }
}
