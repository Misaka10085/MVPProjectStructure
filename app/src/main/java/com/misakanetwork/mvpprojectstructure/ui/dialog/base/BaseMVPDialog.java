package com.misakanetwork.mvpprojectstructure.ui.dialog.base;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;

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
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.xuexiang.xupdate._XUpdate;

import java.io.File;
import java.util.List;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.mvpprojectstructure.ui.base.dialog
 * class name：BaseMVPDialog
 * desc：Dialog中需要网络请求的，继承该Dialog
 */
public abstract class BaseMVPDialog<P extends BaseContract.BasePresenter> extends BaseBottomDialog
        implements BaseContract.BaseView {
    protected P mPresenter = createPresenter();

    protected abstract P createPresenter();

    private VersionDialog versionDialog;
    private RxPermissions rxPermissions;

    /**
     * 网络错误
     */
    @Override
    public void onNetError(String msg) {

        hideLoading();

    }

    @Override
    public void showNetError() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showContent() {

    }

    /**
     * 显示提示语句
     */
    @Override
    public void doPrompt(String msg) {
        synchronized (BaseMVPDialog.class) {
            SingleToastUtils.init(mContext).showNormalApp(msg);
        }
    }

    @Override
    public void onNoLogin(String msg) {

    }

    @Override
    public void upLoadFields(List<NetFileModel> fileModels) {
        SingleToastUtils.init(mContext).showNormalApp(getString(R.string.string_file_upload_success));
        hideLoading();
    }

    @Override
    public void upLoadFieldsFail(String failMsg) {
        SingleToastUtils.init(mContext).showNormalApp(getString(R.string.string_file_upload_failed));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
