package com.misakanetwork.mvpprojectstructure.ui.activity.base;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
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

import io.reactivex.functions.Consumer;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.mvpprojectstructure.ui.base.activity
 * class name：BaseMVPActivity
 * desc：BaseMVPActivity , 需要网络请求则继承该Activity
 */
public abstract class BaseMVPActivity<P extends BaseContract.BasePresenter> extends BaseCenterActivity
        implements BaseContract.BaseView, OnRefreshListener, OnLoadMoreListener {

    protected P mPresenter = createPresenter();

    protected abstract P createPresenter();

    protected SmartRefreshLayout mSmartRefreshLayout;
    protected MultipleStatusView mMultipleStatusView;

    private VersionDialog versionDialog;
    private RxPermissions rxPermissions;

    /**
     * 初始化
     */
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mSmartRefreshLayout = findViewById(R.id.mSmartRefreshLayout);
        mMultipleStatusView = findViewById(R.id.mMultipleStatusView);
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
     * 加载失败点击事件
     */
    protected abstract void netOverride();

    /**
     * 网络错误
     */
    @Override
    public void onNetError(String msg) {
        hideLoading();

        if (mSmartRefreshLayout != null) {
            stopRefresh(false, false);
        }
        showCenterToast(msg);
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

    /**
     * 显示提示语句
     */
    @Override
    public void doPrompt(String msg) {
        synchronized (BaseMVPActivity.class) {
            showCenterToast(msg);
        }
        netErrStopLoading();
    }

    public void netErrStopLoading() {
        hideLoading();

        if (mSmartRefreshLayout != null) {
            stopRefresh(false, false);
        }
    }

    @Override
    public void showEmpty() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showEmpty();
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

    /**
     * 上传成功后的回调
     */
    @Override
    public void upLoadFields(List<NetFileModel> fileModels) {
        SingleToastUtils.init(this).showNormal(getString(R.string.string_file_upload_success));
        hideLoading();
    }

    @Override
    public void upLoadFieldsFail(String failMsg) {
        SingleToastUtils.init(this).showNormal(getString(R.string.string_file_upload_failed));
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
                        if (!VersionHelper.needVersionDialog(BaseMVPActivity.this, Integer.parseInt(versionBean.getCode()))) {
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
                        versionDialog.showThis(getSupportFragmentManager(), VersionDialog.class.getSimpleName(), versionBean, forceJudge);
                    } else {
                        SingleToastUtils.init(BaseMVPActivity.this).showNormalApp(getString(R.string.string_accept_failed));
                    }
                }, new io.reactivex.rxjava3.functions.Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        SingleToastUtils.init(BaseMVPActivity.this).showNormalApp(getString(R.string.string_accept_failed));
                    }
                });
    }

    private void startUpdate(String url, double newVersionCode) {
        if (!BaseAppHelper.getApk().isEmpty() && FileUtils.fileIsExists(BaseAppHelper.getApk())) {
            PackageInfo packageInfo = VersionHelper.getPathInfo(this, BaseAppHelper.getApk());
            int versionCode = packageInfo.versionCode;
            if (versionCode == newVersionCode) { // 本地安装包同版本Code
                _XUpdate.startInstallApk(this, new File(BaseAppHelper.getApk()));
                return;
            } else { // 本地安装包不同版本Code
                FileUtils.deleteDirWithFile(new File(BaseKeywordsHelper.getApkSavedFolder(this)));
            }

        }
        UpdateUtils.updateDownload(this, this, url);
    }

    /**
     * 未登录，返回码在BasePresenterImpl.java中定义
     */
    @Override
    public void onNoLogin(String msg) {
    }

    /**
     * 根据登录存储的token是否存在判断登录状态
     */
    public boolean isValidAccount() {
        if (TextUtils.isEmpty(BaseAppHelper.getToken())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }
}
