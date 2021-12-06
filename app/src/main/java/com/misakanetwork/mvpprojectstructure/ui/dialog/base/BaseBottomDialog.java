package com.misakanetwork.mvpprojectstructure.ui.dialog.base;

import android.view.Gravity;
import android.view.ViewGroup;

import com.misakanetwork.mvpprojectstructure.R;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.mvpprojectstructure.ui.base.dialog
 * class name：BaseBottomDialog
 * desc：底部Dialog弹窗
 */
public abstract class BaseBottomDialog extends BaseDialog {
    @Override
    protected Float setDialogWith() {
        return 1.0F;
    }

    @Override
    protected int setDialogPosition() {
        return Gravity.BOTTOM;
    }

    @Override
    protected int setAnimId() {
        return R.style.popwin_anim_style_bottom;
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
