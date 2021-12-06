package com.misakanetwork.lib_common.utils.screen;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.WindowManager;


/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.utils.screen
 * class name：FullScreenNoBarUtils
 * desc：全面屏（对应activity添加 android:theme="@style/FullScreenTheme"）
 * <p>
 * protected void onCreate(@Nullable Bundle savedInstanceState) {
 * FullScreenNoBarUtils.setFull(this, this);
 * super.onCreate(savedInstanceState);
 * }
 */
public class FullScreenNoBarUtils {

    public static void setFull(Activity activity, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Android P利用官方提供的API适配
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            // 始终允许窗口延伸到屏幕短边上的缺口区域
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            activity.getWindow().setAttributes(lp);
        } else {
            // Android P以下根据手机厂商的适配方案进行适配
            if (RomUtils.isHuawei() && HwNotchUtils.hasNotch(context)) {
                HwNotchUtils.setFullScreenWindowLayoutInDisplayCutout(activity.getWindow());
            } else if (RomUtils.isXiaomi() && XiaomiNotchUtils.hasNotch(context)) {
                XiaomiNotchUtils.setFullScreenWindowLayoutInDisplayCutout(activity.getWindow());
            }
        }
    }
}
