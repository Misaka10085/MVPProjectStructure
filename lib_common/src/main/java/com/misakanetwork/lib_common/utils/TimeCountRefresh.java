package com.misakanetwork.lib_common.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.utils
 * class name：TimeCountRefresh
 * desc：倒计时
 * timer = new TimeCountRefresh(t, 1000, leftTipTv);
 * timer.setOnTimerFinished(this);
 * timer.start();
 */
public class TimeCountRefresh extends CountDownTimer {
    private TextView textView;
    private long millisUntilFinished;

    /**
     * 参数依次为总时长,和计时的时间间隔,要显示的按钮
     *
     * @param millisInFuture    总时长
     * @param countDownInterval 计时的时间间隔
     * @param textView          显示的按钮
     */
    public TimeCountRefresh(long millisInFuture, long countDownInterval, final TextView textView) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
    }

    private OnTimerFinished onTimerFinished;

    public void setOnTimerFinished(OnTimerFinished onTimerFinished) {
        this.onTimerFinished = onTimerFinished;
    }

    @Override
    public void onTick(long millisUntilFinished) { // 计时过程显示
        this.millisUntilFinished = millisUntilFinished;
//        textView.setTextColor(Color.parseColor("#FFFFFF"));
//        textView.setBackgroundResource(R.drawable.send_code_wait);
        textView.setClickable(false);
//        textView.setTextSize((float) 11.5);
        DecimalFormat dec = new DecimalFormat("##.##");
        long day = millisUntilFinished / (1000 * 60 * 60 * 24);
        long hour = millisUntilFinished / (1000 * 60 * 60) - day * 24;
        long min = (millisUntilFinished / (60 * 1000)) - day * 24 * 60 - hour * 60;
        long s = (millisUntilFinished) / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
        textView.setText(min + "′" + s + "″");
//        textView.setText("0" + (int) Math.floor(millisUntilFinished / 60000) + "′" + dec.format((millisUntilFinished % 60000) / 1000) + "″");
//        L.e("onTick===>", "0" + (int) Math.floor(millisUntilFinished / 60000) + "′" + dec.format((millisUntilFinished % 60000) / 1000) + "″");
    }

    @Override
    public void onFinish() { // 计时完毕时触发
        textView.setText("超时");
//        textView.setTextColor(Color.parseColor("#FFFFFF"));
//        textView.setBackgroundResource(R.drawable.send_code);
        textView.setClickable(true);
        if (onTimerFinished != null) {
            onTimerFinished.onTimerFinish();
        }
    }

    public interface OnTimerFinished {
        void onTimerFinish();
    }
}