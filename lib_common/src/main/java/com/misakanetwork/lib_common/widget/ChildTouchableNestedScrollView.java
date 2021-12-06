package com.misakanetwork.lib_common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.widget
 * class name：ChildTouchableNestedScrollView
 * desc：允许子View拦截触摸事件的NestedScrollView
 */
public class ChildTouchableNestedScrollView extends NestedScrollView {
    public ChildTouchableNestedScrollView(@NonNull @org.jetbrains.annotations.NotNull Context context) {
        super(context);
    }

    public ChildTouchableNestedScrollView(@NonNull @org.jetbrains.annotations.NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildTouchableNestedScrollView(@NonNull @org.jetbrains.annotations.NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
