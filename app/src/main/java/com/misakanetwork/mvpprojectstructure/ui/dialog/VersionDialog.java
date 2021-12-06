package com.misakanetwork.mvpprojectstructure.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.misakanetwork.lib_common.entity.VersionBean;
import com.misakanetwork.lib_common.helper.BaseAppHelper;
import com.misakanetwork.lib_common.utils.clickcheck.AntiShake;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.ui.dialog.base.BaseCenterDialog;
import com.ruffian.library.widget.RTextView;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.mvpprojectstructure.ui.dialog
 * class name：VersionDialog
 * desc：版本更新弹窗
 */
public class VersionDialog extends BaseCenterDialog {

    public void showThis(FragmentManager manager, String tag, VersionBean versionBean, boolean forceJudge) {
        super.showThis(manager, tag);
        this.versionBean = versionBean;
        this.forceJudge = forceJudge;
    }

    private VersionBean versionBean = new VersionBean();
    private boolean forceJudge = false; // 是否判断强制更新
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected int setViewId() {
        return R.layout.dialog_version;
    }

    @Override
    protected void initView(View view) {
        TextView contentTv = view.findViewById(R.id.content_tv);
        RTextView updateTv = view.findViewById(R.id.update_tv);
        RTextView cancelTv = view.findViewById(R.id.cancel_tv);
        View lineTwo = view.findViewById(R.id.lineTwo);
        TextView titleTv = view.findViewById(R.id.content_title_tv);
        titleTv.setText(versionBean.getName());
        contentTv.setText(versionBean.getDescription());
        updateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AntiShake.check(v.getId()) && onClickListener != null) {
                    onClickListener.onUpdate();
                }
            }
        });
        cancelTv.setVisibility(forceJudge ? View.GONE : View.VISIBLE);
        lineTwo.setVisibility(forceJudge ? View.GONE : View.VISIBLE);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AntiShake.check(v.getId()) && onClickListener != null) {
                    BaseAppHelper.putLaterUpdate(true);
                    dismissThis(isResumed());
                }
            }
        });
    }

    @Override
    protected void initBundle(Bundle bundle) {
    }

    public interface OnClickListener {
        void onUpdate();
    }

    @Override
    protected boolean setOutSide() {
        return false;
    }

    @Override
    protected boolean clickBack() {
//        return true;
        if (!forceJudge) { // 不判断强制更新，点击返回键可关闭弹窗
            return true;
        }
        return versionBean.getIsUpdate() == 0; // 判断强制更新，isUpdate==1点击返回键不允许返回
    }
}
