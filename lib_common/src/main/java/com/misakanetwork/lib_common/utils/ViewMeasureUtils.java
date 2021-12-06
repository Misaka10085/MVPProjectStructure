package com.misakanetwork.lib_common.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.utils
 * class name：ViewMeasureUtils
 * desc：View测量调整工具类
 */
public class ViewMeasureUtils {

    /**
     * 按dp修改View Margin
     *
     * @param view   View
     * @param left   dp
     * @param top    dp
     * @param right  dp
     * @param bottom dp
     */
    public static void setMarginDp(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (p == null) {
            return;
        }
        int leftCopy = DensityUtils.dp2px(left);
        int topCopy = DensityUtils.dp2px(top);
        int rightCopy = DensityUtils.dp2px(right);
        int bottomCopy = DensityUtils.dp2px(bottom);
        p.setMargins(leftCopy, topCopy, rightCopy, bottomCopy);
        view.requestLayout();
    }

    /**
     * 按px修改View Margin
     *
     * @param view   View
     * @param left   px
     * @param top    px
     * @param right  px
     * @param bottom px
     */
    public static void setMarginPx(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (p == null) {
            return;
        }
        p.setMargins(left, top, right, bottom);
        view.requestLayout();
    }

    /**
     * 按dp修改View高度
     *
     * @param height dp
     * @param view   View
     */
    public static void setVHeightDp(int height, View view) {
        // 获取要改变view的父布局的布局参数
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = DensityUtils.dp2px(height);
        view.setLayoutParams(params);
    }

    /**
     * 按px修改View高度
     *
     * @param height px
     * @param view   View
     */
    public static void setVHeightPx(int height, View view) {
        // 获取要改变view的父布局的布局参数
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }

    /**
     * 按dp修改View宽度
     *
     * @param width dp
     * @param view  View
     */
    public static void setVWidthDp(int width, View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = DensityUtils.dp2px(width);
        view.setLayoutParams(params);
    }

    /**
     * 按px修改View宽度
     *
     * @param width px
     * @param view  View
     */
    public static void setVWidthPx(int width, View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
    }

    /**
     * 获取View高度dp
     *
     * @param view View
     */
    public static int getVHeightDp(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        return (int) DensityUtils.px2dp(params.height);
    }

    /**
     * 获取View高度px
     *
     * @param view View
     */
    public static int getVHeightPx(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        return params.height;
    }

    /**
     * 获取View宽度dp
     *
     * @param view View
     */
    public static int getVWidthDp(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        return (int) DensityUtils.px2dp(params.width);
    }

    /**
     * 获取View宽度px
     *
     * @param view View
     */
    public static int getVWidthPx(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        return params.width;
    }
}
