package com.misakanetwork.lib_common.utils.screen;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.utils.screen
 * class name：VivoNotchUtils
 * desc：Vivo刘海屏适配工具类
 */
public class VivoNotchUtils {

    public static final int VIVO_NOTCH = 0x00000020; // 是否有刘海
    public static final int VIVO_FILLET = 0x00000008; // 是否有圆角

    /**
     * 判断是否有刘海屏
     *
     * @param context
     * @return true：有刘海屏；false：没有刘海屏
     */
    public static boolean hasNotch(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "hasNotchAtVivo ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "hasNotchAtVivo NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "hasNotchAtVivo Exception");
        } finally {
            return ret;
        }
    }
}
