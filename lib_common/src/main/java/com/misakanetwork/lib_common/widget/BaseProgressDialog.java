package com.misakanetwork.lib_common.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.widget
 * class name：BaseProgressDialog
 * desc：自定义ProgressDialog，适配AutoSize框架
 */
public class BaseProgressDialog extends ProgressDialog {

    public BaseProgressDialog(Activity activity, Context context) {
        super(context);
        this.activity = activity;
    }

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 动态设置自定义Dialog的显示内容的宽和高
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.3);   //高度设置为屏幕的0.3
        p.width = d.getWidth();    //宽度设置为全屏
        getWindow().setAttributes(p);     //设置生效
    }
}

