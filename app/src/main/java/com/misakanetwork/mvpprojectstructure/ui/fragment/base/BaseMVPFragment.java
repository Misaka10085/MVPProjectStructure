package com.misakanetwork.mvpprojectstructure.ui.fragment.base;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.classic.common.MultipleStatusView;
import com.misakanetwork.lib_common.entity.NetFileModel;
import com.misakanetwork.lib_common.entity.VersionBean;
import com.misakanetwork.lib_common.helper.BaseAppHelper;
import com.misakanetwork.lib_common.helper.BaseKeywordsHelper;
import com.misakanetwork.lib_common.mvp.BaseContract;
import com.misakanetwork.lib_common.utils.FileUtils;
import com.misakanetwork.lib_common.utils.SingleToastUtils;
import com.misakanetwork.lib_common.utils.VersionHelper;
import com.misakanetwork.lib_common.utils.xupdate.UpdateUtils;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.helper.PermissionContract;
import com.misakanetwork.mvpprojectstructure.ui.dialog.SimpleLoadingDialog;
import com.misakanetwork.mvpprojectstructure.ui.dialog.VersionDialog;
import com.misakanetwork.mvpprojectstructure.utils.NetPathFetchUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.xuexiang.xupdate._XUpdate;

import java.io.File;
import java.util.List;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.mvpprojectstructure.ui.base.fragment
 * class name：BaseMVPFragment
 * desc：需要网络请求的Fragment继承该类
 */
public abstract class BaseMVPFragment<P extends BaseContract.BasePresenter> extends BaseFragment
        implements BaseContract.BaseView, OnRefreshListener, OnLoadMoreListener {

    protected P mPresenter = createPresenter();
    protected SmartRefreshLayout mSmartRefreshLayout;
    protected MultipleStatusView mMultipleStatusView;

    private VersionDialog versionDialog;
    private RxPermissions rxPermissions;

    /**
     * 视图是否加载完毕
     */
    private boolean isViewPrepare = false;

    /**
     * 数据是否加载过了
     */
    private boolean hasLoadData = false;

    private SimpleLoadingDialog simpleLoadingDialog;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewPrepare = true;
        lazyLoadDataIfPrepared();
    }

    private void lazyLoadDataIfPrepared() {
        if (getUserVisibleHint() && isViewPrepare && !hasLoadData) {
            lazyLoad();
            hasLoadData = true;
        }
    }

    @Override
    public void showNetError() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showError();
        }
    }

    @Override
    public void showContent() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showContent();
        }
    }

    @Override
    public void showEmpty() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showEmpty();
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mSmartRefreshLayout = view.findViewById(R.id.mSmartRefreshLayout);
        mMultipleStatusView = view.findViewById(R.id.mMultipleStatusView);
        if (mSmartRefreshLayout != null) {
            mSmartRefreshLayout.setOnLoadMoreListener(this);
            mSmartRefreshLayout.setOnRefreshListener(this);
        }
        if (mMultipleStatusView != null) {
            mMultipleStatusView.setOnRetryClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    netOverride();
                }
            });
        }
    }

    /**
     * 网络错误
     */
    @Override
    public void onNetError(String msg) {
        netErrStopLoading();
        showCenterToast(msg);
    }

    public void netErrStopLoading() {
        hideLoading();

        if (mSmartRefreshLayout != null) {
            stopRefresh(false, false);
        }
    }

    /**
     * 显示提示语句
     *
     * @param msg
     */
    @Override
    public void doPrompt(String msg) {
        synchronized (BaseMVPFragment.class) {
            showCenterToast(msg);
        }
        netErrStopLoading();
    }

    @Override
    public void upLoadFields(List<NetFileModel> fileModels) {
        SingleToastUtils.init(mContext).showNormal(getString(R.string.string_file_upload_success));
        hideLoading();
    }

    @Override
    public void upLoadFieldsFail(String failMsg) {
        SingleToastUtils.init(mContext).showNormal(getString(R.string.string_file_upload_failed));
        hideLoading();
    }

    /**
     * 版本检测结果
     */
    @Override
    public void onVersionChecked(VersionBean versionBean) {
    }

    /**
     * 显示版本弹窗
     */
    @SuppressLint("CheckResult")
    public void showVersionDialog(VersionBean versionBean, boolean forceJudge) {
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        rxPermissions.request(PermissionContract.PHOTO)
                .subscribe(granted -> {
                    if (granted) {
                        if (!VersionHelper.needVersionDialog(mContext, Integer.parseInt(versionBean.getCode()))) {
                            return;
                        }
                        if (versionDialog != null) {
                            versionDialog.dismiss();
                        }
                        versionDialog = new VersionDialog();
                        versionDialog.setOnClickListener(new VersionDialog.OnClickListener() {
                            @Override
                            public void onUpdate() {
                                startUpdate(NetPathFetchUtils.getResUrlPath(versionBean.getFile()), Double.parseDouble(versionBean.getCode()));
                            }
                        });
                        versionDialog.showThis(getChildFragmentManager(), VersionDialog.class.getSimpleName(), versionBean, forceJudge);
                    } else {
                        SingleToastUtils.init(mContext).showNormalApp(getString(R.string.string_accept_failed));
                    }
                }, new io.reactivex.rxjava3.functions.Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        SingleToastUtils.init(mContext).showNormalApp(getString(R.string.string_accept_failed));
                    }
                });
    }

    private void startUpdate(String url, double newVersionCode) {
        if (!BaseAppHelper.getApk().isEmpty() && FileUtils.fileIsExists(BaseAppHelper.getApk())) {
            PackageInfo packageInfo = VersionHelper.getPathInfo(mContext, BaseAppHelper.getApk());
            int versionCode = packageInfo.versionCode;
            if (versionCode == newVersionCode) { // 本地安装包同版本Code
                _XUpdate.startInstallApk(mContext, new File(BaseAppHelper.getApk()));
                return;
            } else { // 本地安装包不同版本Code
                FileUtils.deleteDirWithFile(new File(BaseKeywordsHelper.getApkSavedFolder(mContext)));
            }
        }
        UpdateUtils.updateDownload(getActivity(), mContext, url);
    }

    /**
     * 未登录，返回码在BasePresenterImpl.java中定义
     */
    @Override
    public void onNoLogin(String msg) {
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    protected void stopRefresh(boolean refreshSuc, boolean isLoadMore) {
        if (mSmartRefreshLayout != null) {
            if (mSmartRefreshLayout.getState() == RefreshState.Refreshing) {
                mSmartRefreshLayout.finishRefresh(refreshSuc);
            }

            if (isLoadMore) {
                mSmartRefreshLayout.finishLoadMore().setNoMoreData(false);
            } else {
                mSmartRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }

    protected abstract P createPresenter();

    protected abstract void lazyLoad();

    protected abstract void netOverride();

    public void noticeLogin() {
        // 实现提醒登录逻辑
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!getActivity().isDestroyed()) { // 解决销毁activity后空指针
            mPresenter.detachView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!getActivity().isDestroyed()) { // 解决销毁activity后空指针
            mPresenter.detachView();
        }
    }
}
