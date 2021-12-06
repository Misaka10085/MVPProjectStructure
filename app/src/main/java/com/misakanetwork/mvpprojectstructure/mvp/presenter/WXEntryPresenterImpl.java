package com.misakanetwork.mvpprojectstructure.mvp.presenter;

import com.google.gson.reflect.TypeToken;
import com.misakanetwork.lib_common.entity.BaseObjectModel;
import com.misakanetwork.lib_common.mvp.BasePresenterImpl;
import com.misakanetwork.mvpprojectstructure.mvp.contract.WXEntryContract;
import com.misakanetwork.mvpprojectstructure.mvp.model.WXEntryModelImpl;

import java.util.Map;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.mvp.presenter
 * class name：WXEntryPresenterImpl
 * desc：WXEntryPresenterImpl
 */
public class WXEntryPresenterImpl extends BasePresenterImpl<WXEntryContract.View, WXEntryContract.Model>
        implements WXEntryContract.Presenter {

    @Override
    protected WXEntryContract.Model createModel() {
        return new WXEntryModelImpl();
    }

    public WXEntryPresenterImpl(WXEntryContract.View mView) {
        super(mView);
    }

    @Override
    public void pAccessToken(Map<String, Object> paramsMap) {
        mModel.mAccessToken(new WechatObserver<BaseObjectModel<String>>(new TypeToken<BaseObjectModel<String>>() {
        }.getType()) {

            @Override
            protected void onSuccess(String t) {
                mView.vAccessToken(t);
            }

            @Override
            public void onResultError(String error) {
                mView.doPrompt(error);
                mView.fAccessToken();
            }
        }, paramsMap);
    }

    @Override
    public void pWxUserinfo(Map<String, Object> paramsMap) {
        mModel.mWxUserinfo(new WechatObserver<BaseObjectModel<String>>(new TypeToken<BaseObjectModel<String>>() {
        }.getType()) {

            @Override
            protected void onSuccess(String t) {
                mView.vWxUserinfo(t);
            }

            @Override
            public void onResultError(String error) {
                mView.doPrompt(error);
                mView.fWxUserinfo();
            }
        }, paramsMap);
    }
}
