package com.misakanetwork.lib_common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.widget
 * class name：ParentRecyclerView
 * desc：禁止父recyclerview拦截点击事件
 */
public class ParentRecyclerView extends RecyclerView {

    public ParentRecyclerView(@NonNull Context context) {
        super(context);
    }

    public ParentRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ParentRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // 不拦截，继续分发下去
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }
}
