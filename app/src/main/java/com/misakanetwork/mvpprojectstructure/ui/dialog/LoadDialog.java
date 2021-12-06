package com.misakanetwork.mvpprojectstructure.ui.dialog;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.misakanetwork.lib_common.widget.DYLoadingView;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.ui.dialog.base.BaseCenterDialog;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.mvpprojectstructure.ui.dialog
 * class name：LoadDialog
 * desc：加载动画dialog
 * Activity、Fragment中showLoading(boolean needAni, boolean halfTpValue, boolean bgClickable, boolean clickBack, final String txt)调用
 */
public class LoadDialog extends BaseCenterDialog {
    private AnimationDrawable animationDrawable;
    private TextView loadingTxt;
    private DYLoadingView dyLoadingView;
    public static String NEED_ANI = "NEED_ANI"; // 是否使用DY动画
    private boolean needAni = false;
    public static String HALF_TP_VALUE = "HALF_TP_VALUE"; // 透明度
    private float halfTpValue = 0.5F;
    public static String BG_CLICKABLE = "BG_CLICKABLE"; // 是否可点击背景关闭
    private boolean bgClickable = false;
    public static String CLICK_BACK = "CLICK_BACK"; // 是否允许返回键关闭dialog
    private boolean clickBack = true;

    private LoadingListener loadingListener;

    public void setLoadingListener(LoadingListener loadingListener) {
        this.loadingListener = loadingListener;
    }

    @Override
    protected void initView(View view) {
        loadingTxt = (TextView) view.findViewById(R.id.loading_txt);
        ImageView loadingImageView = (ImageView) view.findViewById(R.id.loading_ImageView);
        if (loadingListener != null) {
            loadingListener.txtShow(loadingTxt);
        }
        if (!needAni) {
            loadingImageView.setImageResource(R.drawable.loading_anim);
            animationDrawable = (AnimationDrawable) loadingImageView.getDrawable();
            animationDrawable.start();
        } else {
            dyLoadingView = view.findViewById(R.id.loading_view);
            dyLoadingView.start();
        }
    }

    @Override
    protected int setViewId() {
        return needAni ? R.layout.dialog_dy_load : R.layout.dialog_load;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        needAni = bundle.getBoolean(NEED_ANI);
        halfTpValue = bundle.getFloat(HALF_TP_VALUE);
        bgClickable = bundle.getBoolean(BG_CLICKABLE);
        clickBack = bundle.getBoolean(CLICK_BACK);
        bundle.clear();
    }

    @Override
    protected void onClickBackStrong() {
        super.onClickBackStrong();
        if (loadingListener != null) {
            loadingListener.onClickBackStrong();
        }
    }

    @Override
    protected boolean setOutSide() {
        return bgClickable; // 禁止外部点击事件
    }

    @Override
    protected float halfTpValue() {
        return halfTpValue;
    }

    @Override
    protected boolean clickBack() {
        return clickBack;
    }

    @Override
    public void dismissThis(boolean isResume) {
        super.dismissThis(isResume);
    }

    public interface LoadingListener {
        void txtShow(TextView loadingTxt);

        void onClickBackStrong(); // clickBack不影响该自实现返回方法，为false仍然触发
    }
}
