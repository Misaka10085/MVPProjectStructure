package com.misakanetwork.lib_common.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created By：Misaka10085
 * on：2021/4/14
 * package：com.misakanetwork.lib_common.utils
 * class name：AnimationUtils
 * desc：动画控制类
 */
public class AnimationUtils {

    public enum AnimationState {
        STATE_SHOW,
        STATE_HIDDEN
    }

    /**
     * 渐隐渐现动画
     *
     * @param view     需要实现动画的对象
     * @param state    需要实现的状态
     * @param duration 动画实现的时长（ms）
     */
    public static void showAndHiddenAnimation(final View view, AnimationState state, long duration, boolean needGone) {
        float start = 0f;
        float end = 0f;
        if (state == AnimationState.STATE_SHOW) {
            end = 1f;
            view.setVisibility(View.VISIBLE);
        } else if (state == AnimationState.STATE_HIDDEN) {
            start = 1f;
            view.setVisibility(needGone ? View.GONE : View.INVISIBLE);
        }
        AlphaAnimation animation = new AlphaAnimation(start, end);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }
        });
        view.setAnimation(animation);
        animation.start();
    }

    /**
     * 中心旋转
     *
     * @param view            View
     * @param duration        duration
     * @param repeatCount     重复次数,-1为永久
     * @param keepFinishState 动画执行完后是否停留在执行完的状态
     * @param waitBefStart    执行前的等待时间
     */
    public static void startRotate(View view, int duration, int repeatCount, boolean keepFinishState, int waitBefStart) {
        RotateAnimation rotate = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(duration); // 设置动画持续周期
        rotate.setRepeatCount(repeatCount == 0 ? -1 : repeatCount); // 设置重复次数
        rotate.setFillAfter(keepFinishState); // 动画执行完后是否停留在执行完的状态
        rotate.setStartOffset(waitBefStart); // 执行前的等待时间
        view.setAnimation(rotate);
        rotate.start();
    }

    /**
     * 从控件所在位置移动到控件的底部
     */
    public static TranslateAnimation moveToViewBottom(int duration) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(duration);
        return mHiddenAction;
    }

    /**
     * 从控件的底部移动到控件所在位置
     */
    public static TranslateAnimation moveToViewLocation(int duration) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(duration);
        return mHiddenAction;
    }
}
