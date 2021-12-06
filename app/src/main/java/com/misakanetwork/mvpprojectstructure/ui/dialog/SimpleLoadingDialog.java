package com.misakanetwork.mvpprojectstructure.ui.dialog;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.ui.dialog.base.BaseCenterDialog;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.mvpprojectstructure.ui.dialog
 * class name：SimpleLoadingDialog
 * desc：无文本加载Dialog
 */
public class SimpleLoadingDialog extends BaseCenterDialog {
    private AnimationDrawable animationDrawable;

    @Override
    protected void initView(View view) {
        ImageView loadingImageView = (ImageView) view.findViewById(R.id.loading_ImageView);
        loadingImageView.setImageResource(R.drawable.loading_anim);
        animationDrawable = (AnimationDrawable) loadingImageView.getDrawable();
        animationDrawable.start();
    }

    @Override
    protected int setViewId() {
        return R.layout.dialog_load_ing;
    }

    @Override
    protected void initBundle(Bundle bundle) {

    }

    @Override
    protected boolean setOutSide() {
        return false;
    }

    @Override
    protected float halfTpValue() {
        return super.halfTpValue();
    }

    @Override
    public void dismissThis(boolean isResume) {
        super.dismissThis(isResume);
    }
}
