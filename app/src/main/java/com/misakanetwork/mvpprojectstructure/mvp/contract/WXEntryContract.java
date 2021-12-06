package com.misakanetwork.mvpprojectstructure.mvp.contract;

import com.misakanetwork.lib_common.mvp.BaseContract;

import java.util.Map;

import io.reactivex.Observer;
import okhttp3.ResponseBody;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.mvp.contract
 * class name：WXEntryContract
 * desc：WXEntryContract
 */
public interface WXEntryContract {
    interface Model extends BaseContract.BaseModel {
        void mAccessToken(Observer<ResponseBody> observer, Map<String, Object> paramsMap);

        void mWxUserinfo(Observer<ResponseBody> observer, Map<String, Object> paramsMap);
    }

    interface Presenter extends BaseContract.BasePresenter {
        void pAccessToken(Map<String, Object> paramsMap);

        void pWxUserinfo(Map<String, Object> paramsMap);
    }

    interface View extends BaseContract.BaseView {
        void vAccessToken(String jsonStr);

        void fAccessToken();

        void vWxUserinfo(String jsonStr);

        void fWxUserinfo();
    }
}
