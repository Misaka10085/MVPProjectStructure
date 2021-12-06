package com.yzq.zxinglibrary.android;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * date：2017/12/21 0021 on 17:28
 * desc:${沉浸式状态栏工具类}
 * author:BarryL
 */
public class WindowUtil {

    public static void TransparentStatusBar(Activity activity){
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
