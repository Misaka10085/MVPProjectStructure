package com.misakanetwork.lib_common.utils;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import me.jessyan.autosize.AutoSize;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.utils
 * class name：CustomToastUtils
 * desc：CustomToastUtils
 */
public class CustomToastUtils {
    private Context context;
    private Toast mToast;
    private TextView mTextView;
    private ImageView mImageView;
    private TimeCount timeCount;
    private String message;
    private Handler mHandler = new Handler();
    private boolean canceled = true;

    public static final String CENTER = "CENTER";
    public static final String TOP = "TOP";
    public static final String END = "END";
    public static final String START = "START";
    public static final String TOP_Y = "TOP_Y"; // 默认y offset 150
    public static final String BOTTOM = "BOTTOM";

    /**
     * @param context   context
     * @param layoutId  自定义布局id
     * @param contentId 布局TextView id
     * @param imgId     布局图片 id，可为0
     * @param place     Toast位置
     * @param msg       自定义TextView消息内容
     * @param imgLength 解析后的图片全地址，可为空
     */
    public CustomToastUtils(Context context, int layoutId, int contentId, int imgId, @NotNull String place,
                            String msg, String imgLength, int res) {
        this.context = context;
        message = msg;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 自定义布局
        View view = inflater.inflate(layoutId, null);
        // 自定义toast文本
        mTextView = (TextView) view.findViewById(contentId);
        mTextView.setText(msg);
        if (imgLength != null) {
            RequestOptions rOptions = new RequestOptions().circleCrop();
            mImageView = (ImageView) view.findViewById(imgId);
            Glide.with(context).load(imgLength).apply(rOptions).into(mImageView);
        } else if (res != 0) {
            mImageView = (ImageView) view.findViewById(imgId);
            mImageView.setImageResource(res);
        }
        L.e("ToastUtil", "Toast start...");
        if (mToast == null) {
            mToast = new Toast(context);
            L.e("ToastUtil", "Toast create...");
        }
        // 设置toast居中显示
        switch (place) {
            case CENTER:
                mToast.setGravity(Gravity.CENTER, 0, -StatusBarUtils.getStatusBarHeight(context));
                break;
            case TOP:
                mToast.setGravity(Gravity.TOP, 0, 0);
                break;
            case END:
                mToast.setGravity(Gravity.END, 0, 0);
                break;
            case START:
                mToast.setGravity(Gravity.START, 0, 0);
                break;
            case TOP_Y:
                mToast.setGravity(Gravity.TOP, 0, 150);
                break;
            case BOTTOM:
                mToast.setGravity(Gravity.BOTTOM, 0, 0);
                break;
        }
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setView(view);
    }

    /**
     * 自定义居中显示toast
     */
    public void show() {
        AutoSize.autoConvertDensityOfGlobal((Activity) context);
        mToast.show();
        L.e("ToastUtil", "Toast show...");
    }

    public void showApp() {
        mToast.show();
        L.e("ToastUtil", "Toast show...");
    }

    /**
     * 自定义时长、居中显示toast
     *
     * @param duration 单位毫秒ms
     */
    public void show(int duration) {
        AutoSize.autoConvertDensityOfGlobal((Activity) context);
        timeCount = new TimeCount(duration, 1000);
        L.e("ToastUtil", "Toast show...");
        if (canceled) {
            timeCount.start();
            canceled = false;
            showUntilCancel();
        }
    }

    public void showApp(int duration) {
        timeCount = new TimeCount(duration, 1000);
        L.e("ToastUtil", "Toast show...");
        if (canceled) {
            timeCount.start();
            canceled = false;
            showUntilCancel();
        }
    }

    /**
     * 隐藏toast
     */
    public void hide() {
        if (mToast != null) {
            mToast.cancel();
        }
        canceled = true;
        L.e("ToastUtil", "Toast that customed duration hide...");
    }

    private void showUntilCancel() {
        if (canceled) { //如果已经取消显示，就直接return
            return;
        }
        mToast.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                L.e("ToastUtil", "Toast showUntilCancel...");
                showUntilCancel();
            }
        }, Toast.LENGTH_LONG);
    }


    /**
     * 自定义计时器
     */
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval); //millisInFuture总计时长，countDownInterval时间间隔(一般为1000ms)
        }

        @Override
        public void onTick(long millisUntilFinished) {
//            mTextView.setText(message + ": " + millisUntilFinished / 1000 + "s后消失");
        }

        @Override
        public void onFinish() {
            hide();
        }
    }
}
