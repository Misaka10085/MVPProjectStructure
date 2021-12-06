package com.misakanetwork.mvpprojectstructure.ui.dialog.base;

import android.view.Gravity;
import android.view.ViewGroup;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.mvpprojectstructure.ui.base.dialog
 * class name：BaseCenterDialog
 * desc：BaseCenterDialog
 */
public abstract class BaseCenterDialog extends BaseDialog{
    @Override
    protected Float setDialogWith() {
        return 0.85F;
    }

    @Override
    protected int setDialogPosition() {
        return Gravity.CENTER;
    }

    @Override
    protected int setAnimId() {
        return android.R.style.Animation_Dialog;
    }

    @Override
    protected boolean setOutSide() {
        return true;
    }

    @Override
    protected boolean clickBack() {
        return true;
    }

    @Override
    protected float halfTpValue() {
        return 0.5F;
    }

    @Override
    protected int setDialogHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected float setHeight() {
        return 0;
    }
}
