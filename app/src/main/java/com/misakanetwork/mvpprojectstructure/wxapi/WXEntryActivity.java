package com.misakanetwork.mvpprojectstructure.wxapi;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.misakanetwork.lib_common.entity.MessageEvent;
import com.misakanetwork.lib_common.utils.rx.RxBus;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.mvp.contract.WXEntryContract;
import com.misakanetwork.mvpprojectstructure.mvp.presenter.WXEntryPresenterImpl;
import com.misakanetwork.mvpprojectstructure.ui.activity.base.BaseMVPActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.wxapi
 * class name：WXEntryActivity
 * desc 微信混淆需要混淆具体bean
 * <p>
 * login/getInfo:
 * IWXAPI api = WXAPIFactory.createWXAPI(this, WXConfig.APP_ID_WX, true);
 * api.registerApp(WXConfig.APP_ID_WX);
 * SendAuth.Req req = new SendAuth.Req();
 * req.scope = "snsapi_userinfo";
 * req.state = WXConfig.APP_STATE_WX;
 * api.sendReq(req);
 */
public class WXEntryActivity extends BaseMVPActivity<WXEntryPresenterImpl> implements WXEntryContract.View, IWXAPIEventHandler {
    private static IWXAPI iwxapi;
    private String preCode = ""; // 记录每次返回的Code，防止重复接收同一Code
    private int codeCount = 0;

    public static final String WX_ENTRY_DATA_CODE = "WX_ENTRY_DATA_CODE"; // wx code
    public static final MessageEvent infoEvent = new MessageEvent(MessageEvent.EVENT_WX_INFO); // 微信接口返回信息event
    public static final String EVENT_DATA_OPEN_ID = "EVENT_DATA_OPEN_ID"; // EVENT_WX_INFO openId
    public static final String EVENT_DATA_NICK_NAME = "EVENT_DATA_NICK_NAME"; // EVENT_WX_INFO nickName

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerRxBus();
//        getSupportActionBar().hide();
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 接收到分享以及登录的intent传递handleIntent方法，处理结果
        iwxapi = WXAPIFactory.createWXAPI(this, WXConfig.APP_ID_WX, false);
        iwxapi.handleIntent(getIntent(), this);
        try {
            boolean b = iwxapi.handleIntent(getIntent(), this);
            if (!b) {
                finish();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    //请求回调结果处理
    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                switch (baseResp.getType()) {
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX: // 分享
                        finish();
                        break;
                    case ConstantsAPI.COMMAND_SENDAUTH: // 登录授权
                        SendAuth.Resp resp = (SendAuth.Resp) baseResp;
                        codeCount++;
                        if (preCode.equals(resp.code)) {
                            return;
                        } else if (codeCount > 1) {
                            return;
                        }
                        HashMap<String, Object> paramsMap = new HashMap<>();
                        paramsMap.put("appid", WXConfig.APP_ID_WX);
                        paramsMap.put("secret", WXConfig.APP_SECRET_WX);
                        paramsMap.put("code", resp.code);
                        paramsMap.put("grant_type", WXConfig.GRAN_TYPE);
                        mPresenter.pAccessToken(paramsMap);
//                        MessageEvent codeEvent = new MessageEvent(MessageEvent.EVENT_GET_WX_CODE);
//                        codeEvent.put(WX_ENTRY_DATA_CODE, resp.code);
//                        RxBus.getInstance().post(codeEvent);
                        preCode = resp.code;
//                        finish();
                        break;
                    default:
                        finish();
                        break;
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED: // 用户拒绝授权
                Toast.makeText(getApplicationContext(), R.string.string_user_denied, Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL: // 用户取消
                Toast.makeText(getApplicationContext(), R.string.string_user_cancel, Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                finish();
                break;
        }
    }

    @Override
    protected void netOverride() {
    }

    @Override
    protected WXEntryPresenterImpl createPresenter() {
        return new WXEntryPresenterImpl(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        registerRxBus();
    }

    @Override
    public void vAccessToken(String jsonStr) {
        Gson gson = new Gson();
        WxBean wxBean = gson.fromJson(jsonStr, new TypeToken<WxBean>() {
        }.getType());
        if (wxBean.getAccess_token().isEmpty() || wxBean.getOpenid().isEmpty()) {
            showCenterToast(getString(R.string.string_get_wx_info_failed));
            finish();
        } else {
            infoEvent.put(EVENT_DATA_OPEN_ID, wxBean.getOpenid());
            HashMap<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("access_token", wxBean.getAccess_token());
            paramsMap.put("openid", wxBean.getOpenid());
            mPresenter.pWxUserinfo(paramsMap);
        }
    }

    @Override
    public void vWxUserinfo(String jsonStr) {
        Gson gson = new Gson();
        WxUserBean wxUserBean = gson.fromJson(jsonStr, new TypeToken<WxUserBean>() {
        }.getType());
        infoEvent.put(EVENT_DATA_NICK_NAME, wxUserBean.getNickname());
        RxBus.getInstance().post(infoEvent);
        finish();
    }

    @Override
    public void fAccessToken() {
        showCenterToast(getString(R.string.string_wx_data_error));
        finish();
    }

    @Override
    public void fWxUserinfo() {
        showCenterToast(getString(R.string.string_wx_data_error));
        finish();
    }
}
