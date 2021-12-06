package com.misakanetwork.mvpprojectstructure.wxapi;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.misakanetwork.lib_common.entity.MessageEvent;
import com.misakanetwork.lib_common.utils.ActivityController;
import com.misakanetwork.lib_common.utils.L;
import com.misakanetwork.lib_common.utils.rx.RxBus;
import com.misakanetwork.mvpprojectstructure.MainApplication;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.ui.activity.base.BaseCenterActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.wxapi
 * class name：WXPayEntryActivity
 * desc：微信混淆需要混淆具体bean
 */
public class WXPayEntryActivity extends BaseCenterActivity implements IWXAPIEventHandler {


    private IWXAPI api;

    public static String APPID = WXConfig.APP_ID_WX;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        registerRxBus();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                showCenterToast(getString(R.string.string_pay_suc));
                RxBus.getInstance().post(new MessageEvent(MessageEvent.EVENT_WX_PAYMENT_SUCCESS)); // 支付完成
                ActivityController.getInstance().finishActivity();
            } else {
                L.e("wxPayment", ">>> onResp: " + resp.errCode);
                showCenterToast(getString(R.string.string_pay_fai));
                RxBus.getInstance().post(new MessageEvent(MessageEvent.EVENT_WX_PAYMENT_FAILED));
            }
            finish();
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }
}
