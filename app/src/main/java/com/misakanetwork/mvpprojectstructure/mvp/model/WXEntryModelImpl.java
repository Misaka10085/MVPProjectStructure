package com.misakanetwork.mvpprojectstructure.mvp.model;

import com.misakanetwork.lib_common.mvp.BaseModelImpl;
import com.misakanetwork.lib_common.net.NetManager;
import com.misakanetwork.lib_common.utils.rx.RxHelper;
import com.misakanetwork.mvpprojectstructure.mvp.contract.WXEntryContract;

import java.util.Map;

import io.reactivex.Observer;
import okhttp3.ResponseBody;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.mvp.model
 * class name：WXEntryModelImpl
 * desc：WXEntryModelImpl
 */
public class WXEntryModelImpl extends BaseModelImpl implements WXEntryContract.Model {
    @Override
    public void mAccessToken(Observer<ResponseBody> observer, Map<String, Object> paramsMap) {
        NetManager.getInstance()
                .getWxApiService()
                .wxAccessToken(paramsMap)
                .compose(RxHelper.<ResponseBody>rxSchedulerHelper())
                .subscribe(observer);
    }

    @Override
    public void mWxUserinfo(Observer<ResponseBody> observer, Map<String, Object> paramsMap) {
        NetManager.getInstance()
                .getWxApiService()
                .wxUserinfo(paramsMap)
                .compose(RxHelper.<ResponseBody>rxSchedulerHelper())
                .subscribe(observer);
    }
}
