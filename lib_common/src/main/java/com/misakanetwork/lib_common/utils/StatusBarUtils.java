package com.misakanetwork.lib_common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created By：Misaka10085
 * on：2021/4/14
 * package：com.misakanetwork.lib_common.utils
 * class name：StatusBarUtils
 * desc：状态栏工具类
 */
public class StatusBarUtils {

    /**
     * 获取状态栏高度
     *
     * @return px
     */
    public static int getStatusBarHeight(Context context) {
        int result = 24;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        } else {
            result = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    result, Resources.getSystem().getDisplayMetrics());
        }
        return result;
    }

    /**
     * init自定义View状态栏，沉浸式模式下创建View使用，不受setWindowStatusBarColor()影响
     */
    public static void initHeight(Context context, View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            lp.height = getStatusBarHeight(context);
        }
        view.setLayoutParams(lp);
    }

    /**
     * 设置系统状态栏颜色
     *
     * @param activity         activity
     * @param colorResId       状态栏颜色
     * @param bottomColorResId 底部导航栏颜色
     */
    public static void setWindowStatusBarColor(Activity activity, int colorResId, Integer bottomColorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));
                // 底部导航栏
                if (bottomColorResId != null) {
                    window.setNavigationBarColor(activity.getResources().getColor(bottomColorResId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
