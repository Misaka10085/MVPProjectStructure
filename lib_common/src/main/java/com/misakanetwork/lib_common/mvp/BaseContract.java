package com.misakanetwork.lib_common.mvp;

import com.misakanetwork.lib_common.entity.MultipleFileInterface;
import com.misakanetwork.lib_common.entity.NetFileModel;
import com.misakanetwork.lib_common.entity.VersionBean;

import java.util.List;

import io.reactivex.Observer;
import okhttp3.ResponseBody;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.mvp
 * class name：BaseContract
 * desc：BaseContract
 */
public interface BaseContract {

    interface BaseModel {

        /*多个文件上传*/
        void mUpLoadField(Observer<ResponseBody> observer, List<MultipleFileInterface> fileList);

        /*检测版本*/
        void mCheckVersion(Observer<ResponseBody> observer);
    }

    interface BasePresenter {

        /*解绑View*/
        void detachView();

        /*多文件上传*/
        void pUpLoadField(List<MultipleFileInterface> fileList);

        /*检测版本*/
        void pCheckVersion();
    }

    interface BaseView {

        /*上传多个图片回调*/
        void upLoadFields(List<NetFileModel> fileModels);

        /*上传图片失败*/
        void upLoadFieldsFail(String failMsg);

        /*网络错误*/
        void onNetError(String msg);

        /*未登录*/
        void onNoLogin(String msg);

        /*报提示*/
        void doPrompt(String msg);

        /*显示加载弹窗*/
        void showLoading(boolean isShow, float halfTpValue, boolean bgClickable, boolean clickBack, String content);

        /*关闭加载弹窗*/
        void hideLoading();

        /*显示网络错误布局*/
        void showNetError();

        /*显示空布局*/
        void showEmpty();

        /*显示内容布局*/
        void showContent();

        /*检测版本*/
        void onVersionChecked(VersionBean versionBean);
    }
}
