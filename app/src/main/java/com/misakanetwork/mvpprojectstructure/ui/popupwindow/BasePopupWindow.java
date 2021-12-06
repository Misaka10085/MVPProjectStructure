package com.misakanetwork.mvpprojectstructure.ui.popupwindow;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.ui.popupwindow
 * class name：BasePopupWindow
 * desc：BasePopupWindow
 */
public abstract class BasePopupWindow<T> extends PopupWindow {

    protected Context context;

    //是否需要背景变暗的动画，PopupWindow默认背景不会变暗，但多数需求需要变暗，这里默认true
    protected boolean needAnim = true;

    protected List<T> results;


    //背景遮罩层，如果设置了遮罩层，那么背景的变化将使用遮罩层实现，否则将默认使用系统窗口alpha值变化实现效果
    protected View shadowView;

    public BasePopupWindow(Context context, List<T> results) {
        this.context = context;
        this.results = results;

        setContentView(LayoutInflater.from(context).inflate(getLayout(), null));

        //获取屏幕宽高
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        int w = outMetrics.widthPixels;
        int h = outMetrics.heightPixels;

        // 设置SelectPicPopupWindow的View
        this.setContentView(getContentView());
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(setWindowHeight());
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimationPreview);
        ButterKnife.bind(getContentView());
        initBase(getContentView(), results);
    }

    protected abstract int setWindowHeight();

    public abstract int getLayout();

    public abstract void initBase(View view, List<T> results);

    public View holder(int id) {
        return getContentView().findViewById(id);
    }

    /**
     * 显示popupWindow
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            int x = (parent.getWidth() - this.getWidth()) / 2;
            this.showAsDropDown(parent, x, 0);
            if (needAnim) turnBackgroundDark();
        } else {
            this.dismiss();
        }
    }

    public void showPopupWindowS(View parent) {
        if (!this.isShowing()) {
//            int x = (parent.getWidth() - this.getWidth()) / 2;
//            this.showAsDropDown(parent, x, 0);
            this.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            if (needAnim) turnBackgroundDark();
        } else {
            this.dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (needAnim) resetBackground();
    }
    ////////////////////////
    //////////对外接口
    ////////////////////////

    public void setNeedAnim(boolean needAnim) {
        this.needAnim = needAnim;
    }

    public void setShadowView(View shadowView) {
        this.shadowView = shadowView;
    }

    //////////////////////////
    ////////背景变暗
    //////////////////////////
    protected void turnBackgroundDark() {
        turnDark();
    }

    protected void resetBackground() {
        turnLight();
    }


    ////////////////////////
    //////背景变暗动画
    ////////////////////////

    private void turnDark() {
        if (shadowView != null) {
            shadowView.setVisibility(View.VISIBLE);
            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.setDuration(300).start();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float alpha = (Float) animation.getAnimatedValue();
                    shadowView.setAlpha(alpha);
                }
            });
        } else {
            final Activity activity = (Activity) context;
            ValueAnimator animator = ValueAnimator.ofFloat(1f, 0.7f);
            animator.setDuration(300).start();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                    params.alpha = (Float) animation.getAnimatedValue();
                    activity.getWindow().setAttributes(params);
                }
            });
        }
    }

    private void turnLight() {
        if (shadowView != null) {
            ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f);
            animator.setDuration(300).start();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float alpha = (Float) animation.getAnimatedValue();
                    shadowView.setAlpha(alpha);
                }
            });
        } else {
            final Activity activity = (Activity) context;
            ValueAnimator animator = ValueAnimator.ofFloat(0.7f, 1f);
            animator.setDuration(300).start();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                    params.alpha = (Float) animation.getAnimatedValue();
                    activity.getWindow().setAttributes(params);
                }
            });
        }
    }
}
