package com.misakanetwork.lib_common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Created By：Misaka10085
 * on：2021/7/13
 * package：com.misakanetwork.lib_common.widget
 * class name：ScrollControllableViewPager
 * desc：动态控制能否滑动的ViewPager
 */
public class ScrollControllableViewPager extends ViewPager {
    private boolean scrollable = true;

    public ScrollControllableViewPager(@NonNull Context context) {
        super(context);
    }

    public ScrollControllableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollable) {
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (scrollable == false) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }
}

