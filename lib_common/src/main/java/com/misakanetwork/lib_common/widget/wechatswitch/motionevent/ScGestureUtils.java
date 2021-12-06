package com.misakanetwork.lib_common.widget.wechatswitch.motionevent;

import android.view.MotionEvent;

import com.misakanetwork.lib_common.utils.L;

/**
 * Created By：Misaka10085
 * on：2021/7/15
 * package：com.misakanetwork.lib_common.widget.wechatswitch.motionevent
 * class name：ScGestureUtils
 * desc：手势判断工具类
 */
public class ScGestureUtils {
    private static final String TAG = "ScGestureUtils";

    private float startX = 0f;
    private float endX = 0f;
    private float startY = 0f;
    private float endY = 0f;
    private float xDistance = 0f;
    private float yDistance = 0f;

    public enum Gesture {
        PullUp, PullDown, PullLeft, PullRight
    }

    public ScGestureUtils() {

    }

    /**
     * 当event.getAction() == MotionEvent.ACTION_DOWN 的时候调用
     * 设置初始X,Y坐标
     */
    public void actionDown(MotionEvent event) {
        xDistance = yDistance = 0f;
        setStartX(event);
        setStartY(event);
    }

    /**
     * 当event.getAction() == MotionEvent.ACTION_MOVE 的时候调用
     * 设置移动的X,Y坐标
     */
    public void actionMove(MotionEvent event) {
        setEndX(event);
        setEndY(event);
    }

    /**
     * 当event.getAction() == MotionEvent.ACTION_UP 的时候调用
     * 设置截止的X,Y坐标
     */
    public void actionUp(MotionEvent event) {
        setEndX(event);
        setEndY(event);
    }

    /**
     * 手势判断接口
     */
    public boolean getGesture(Gesture gesture) {
        switch (gesture) {
            case PullUp:
                return isRealPullUp();
            case PullDown:
                return isRealPullDown();
            case PullLeft:
                return isRealPullLeft();
            case PullRight:
                return isRealPullRight();
            default:
                L.e(TAG, "getGesture error");
                return false;
        }
    }

    /**
     * 获取Touch点相对于屏幕原点的X坐标
     */
    private float gestureRawX(MotionEvent event) {
        return event.getRawX();
    }

    /**
     * 获取Touch点相对于屏幕原点的Y坐标
     */
    private float gestureRawY(MotionEvent event) {
        return event.getRawY();
    }

    /**
     * 获取X轴偏移量,取绝对值
     */
    private float gestureDistanceX(float startX, float endX) {
        setxDistance(Math.abs(endX - startX));
        return xDistance;
    }

    /**
     * 获取Y轴偏移量,取绝对值
     */
    private float gestureDistanceY(float startY, float endY) {
        setyDistance(Math.abs(endY - startY));
        return yDistance;
    }

    /**
     * endY坐标比startY小,相减负数表示手势上滑
     */
    private boolean isPullUp(float startY, float endY) {
        return (endY - startY) < 0;
    }

    /**
     * endY坐标比startY大,相减正数表示手势下滑
     */
    private boolean isPullDown(float startY, float endY) {
        return (endY - startY) > 0;
    }

    /**
     * endX坐标比startX大,相减正数表示手势右滑
     */
    private boolean isPullRight(float startX, float endX) {
        return (endX - startX) > 0;
    }

    /**
     * endX坐标比startX小,相减负数表示手势左滑
     */
    private boolean isPullLeft(float startX, float endX) {
        return (endX - startX) < 0;
    }

    /**
     * 判断用户真实操作是否是上滑
     */
    private boolean isRealPullUp() {
        if (gestureDistanceX(startX, endX) < gestureDistanceY(startY, endY)) {
            //Y轴偏移量大于X轴,表示用户真实目的是上下滑动
            return isPullUp(startY, endY);
        }
        return false;
    }

    /**
     * 判断用户真实操作是否是下滑
     */
    private boolean isRealPullDown() {
        if (gestureDistanceX(startX, endX) < gestureDistanceY(startY, endY)) {
            //Y轴偏移量大于X轴,表示用户真实目的是上下滑动
            return isPullDown(startY, endY);
        }
        return false;
    }

    /**
     * 判断用户真实操作是否是左滑
     */
    private boolean isRealPullLeft() {
        if (gestureDistanceX(startX, endX) > gestureDistanceY(startY, endY)) {
            //Y轴偏移量大于X轴,表示用户真实目的是上下滑动
            return isPullLeft(startX, endX);
        }
        return false;
    }

    /**
     * 判断用户真实操作是否是右滑
     */
    private boolean isRealPullRight() {
        if (gestureDistanceX(startX, endX) > gestureDistanceY(startY, endY)) {
            //Y轴偏移量大于X轴,表示用户真实目的是上下滑动
            return isPullRight(startX, endX);
        }
        return false;
    }


    private ScGestureUtils setStartX(MotionEvent event) {
        this.startX = gestureRawX(event);
        return this;
    }

    private ScGestureUtils setEndX(MotionEvent event) {
        this.endX = gestureRawX(event);
        return this;
    }

    private ScGestureUtils setStartY(MotionEvent event) {
        this.startY = gestureRawY(event);
        return this;
    }

    private ScGestureUtils setEndY(MotionEvent event) {
        this.endY = gestureRawY(event);
        return this;
    }

    private ScGestureUtils setxDistance(float xDistance) {
        this.xDistance = xDistance;
        return this;
    }

    private ScGestureUtils setyDistance(float yDistance) {
        this.yDistance = yDistance;
        return this;
    }

    public float getStartX() {
        return startX;
    }

    public float getEndX() {
        return endX;
    }

    public float getStartY() {
        return startY;
    }

    public float getEndY() {
        return endY;
    }

    public float getxDistance() {
        return xDistance;
    }

    public float getyDistance() {
        return yDistance;
    }
}
