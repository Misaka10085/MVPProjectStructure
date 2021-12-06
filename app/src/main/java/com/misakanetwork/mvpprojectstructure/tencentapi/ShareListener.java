package com.misakanetwork.mvpprojectstructure.tencentapi;

import android.content.Context;

import com.misakanetwork.lib_common.utils.SingleToastUtils;
import com.misakanetwork.mvpprojectstructure.R;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.tencentapi
 * class name：ShareListener
 * desc：ShareListener
 */
public class ShareListener implements IUiListener {
    private Context context;

    public ShareListener(Context context) {
        this.context = context;
    }

    @Override
    public void onCancel() {
        SingleToastUtils.init(context).showNormalApp(context.getString(R.string.string_share_cancel));
    }

    @Override
    public void onComplete(Object arg0) {
        SingleToastUtils.init(context).showNormalApp(context.getString(R.string.string_share_success));
    }

    @Override
    public void onError(UiError arg0) {
        SingleToastUtils.init(context).showNormalApp(context.getString(R.string.string_share_failed));
    }
}
