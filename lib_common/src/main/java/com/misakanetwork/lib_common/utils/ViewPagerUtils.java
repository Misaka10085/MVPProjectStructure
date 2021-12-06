package com.misakanetwork.lib_common.utils;

import android.view.MotionEvent;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.lib_common.utils
 * class name：ViewPagerUtils
 * desc：ViewPagerUtils
 */
public class ViewPagerUtils {
    private static float originX = 0;
    private static float originY = 0;

    /**
     * ViewPager滑动角度控制
     * mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
     *
     * @Override public boolean onTouch(View v, MotionEvent event) {
     * ViewPagerUtils.viewPagerControl(event);
     * return false;
     * }
     * });
     */
    public static void viewPagerControl(MotionEvent event) {
        float cX = 0;
        float cY = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                originY = event.getY();
                originX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                cY = event.getY();
                cX = event.getX();
                float distanceX = Math.abs(cX - originX);
                float distanceY = Math.abs(cY - originY);
            case MotionEvent.ACTION_UP:

                break;
        }
    }

    private static double getAngle(float distanceX, float distanceY) {
        double angle = 0;
        angle = Math.atan2(distanceY, distanceX) * 180 / Math.PI;
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }
}
