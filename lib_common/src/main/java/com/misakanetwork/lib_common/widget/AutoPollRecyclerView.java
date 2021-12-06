package com.misakanetwork.lib_common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

/**
 * Created By：Misaka10085
 * on：2021/7/7
 * package：com.misakanetwork.lib_common.widget
 * class name：AutoPollRecyclerView
 * desc：自动滚动RecyclerView
 * <p>
 * 速度推荐：
 * switch (view.getId()) {
 * case R.id.speed_025_tv:
 * mRecyclerView.TIME_AUTO_POLL = 150;
 * mRecyclerView.speed = (int) Math.ceil(pullSpeed); // 0.25倍速
 * break;
 * case R.id.speed_05_tv:
 * mRecyclerView.TIME_AUTO_POLL = 50;
 * mRecyclerView.speed = (int) Math.ceil(pullSpeed); // 0.5倍速
 * break;
 * case R.id.speed_1_tv:
 * mRecyclerView.TIME_AUTO_POLL = 25;
 * mRecyclerView.speed = (int) Math.ceil(pullSpeed); // 1倍速
 * break;
 * case R.id.speed_15_tv:
 * mRecyclerView.TIME_AUTO_POLL = 35;
 * mRecyclerView.speed = (int) Math.ceil(pullSpeed * 1.5); // 1.5倍速
 * break;
 * case R.id.speed_2_tv:
 * mRecyclerView.TIME_AUTO_POLL = 25;
 * mRecyclerView.speed = (int) Math.ceil(pullSpeed * 2); // 2倍速
 * break;
 * }
 */
public class AutoPollRecyclerView extends RecyclerView {
    public long TIME_AUTO_POLL = 18;
    public int speed = 4;
    public AutoPollTask autoPollTask;
    public boolean running; // 标示是否正在自动轮询
    public boolean canRun; // 标示是否可以自动轮询,可在不需要的是否置false

    public AutoPollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        autoPollTask = new AutoPollTask(this);
    }

    class AutoPollTask implements Runnable {
        private final WeakReference<AutoPollRecyclerView> mReference;

        //使用弱引用持有外部类引用->防止内存泄漏
        public AutoPollTask(AutoPollRecyclerView reference) {
            this.mReference = new WeakReference<AutoPollRecyclerView>(reference);
        }

        @Override
        public void run() {
            if (onRunningListener != null) {
                onRunningListener.onRunning();
            }
            AutoPollRecyclerView recyclerView = mReference.get();
            if (recyclerView != null && recyclerView.running && recyclerView.canRun) {
                recyclerView.scrollBy(2, speed);
                recyclerView.postDelayed(recyclerView.autoPollTask, recyclerView.TIME_AUTO_POLL);
            }
        }
    }

    private OnRunningListener onRunningListener;

    public void setOnRunningListener(OnRunningListener onRunningListener) {
        this.onRunningListener = onRunningListener;
    }

    public interface OnRunningListener {
        void onRunning();
    }

    //开启:如果正在运行,先停止->再开启
    public void start() {
        if (running)
            stop();
        canRun = true;
        running = true;
        postDelayed(autoPollTask, TIME_AUTO_POLL);
    }

    public void stop() {
        running = false;
        canRun = false;
        removeCallbacks(autoPollTask);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (running)
                    stop();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                if (canRun)
                    start();
                break;
        }
        return super.onTouchEvent(e);
    }

    /**
     * RecyclerView滑动位置判断
     *
     * @param direction 1,false->滑动到底部
     *                  -1,true->滑动到顶部
     */
    public boolean canScrollVertically(int direction) {
        final int offset = computeVerticalScrollOffset();
        final int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) {
            return offset > 0;
        } else {
            return offset < range - 1;
        }
    }
}
