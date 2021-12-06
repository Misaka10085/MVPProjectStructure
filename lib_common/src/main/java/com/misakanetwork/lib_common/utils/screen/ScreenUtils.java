package com.misakanetwork.lib_common.utils.screen;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import static android.view.View.NO_ID;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.utils
 * class name：ScreenUtils
 * desc：ScreenUtils
 */
public class ScreenUtils {
    private static final String NAVIGATION = "navigationBarBackground";

    /**
     * 当前是否为全面屏 在onWindowFocusChanged中调用
     */
    // 该方法需要在View完全被绘制出来之后调用，否则判断不了
    // 在比如 onWindowFocusChanged（）方法中可以得到正确的结果
    public static boolean isNavigationBarExist(@NonNull Activity activity) {
        ViewGroup vp = (ViewGroup) activity.getWindow().getDecorView();
        if (vp != null) {
            for (int i = 0; i < vp.getChildCount(); i++) {
                vp.getChildAt(i).getContext().getPackageName();
                if (vp.getChildAt(i).getId() != NO_ID && NAVIGATION.equals(activity.getResources().getResourceEntryName(vp.getChildAt(i).getId()))) {
                    return true;
                }
            }
        }
        return false;
    }
}
