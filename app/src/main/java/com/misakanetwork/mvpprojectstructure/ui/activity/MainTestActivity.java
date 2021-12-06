package com.misakanetwork.mvpprojectstructure.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.misakanetwork.lib_common.entity.LocalFileModel;
import com.misakanetwork.lib_common.entity.MultipleFileInterface;
import com.misakanetwork.lib_common.entity.NetFileModel;
import com.misakanetwork.lib_common.entity.VersionBean;
import com.misakanetwork.lib_common.helper.BaseAppHelper;
import com.misakanetwork.lib_common.helper.BaseKeywordsHelper;
import com.misakanetwork.lib_common.utils.FileUtils;
import com.misakanetwork.lib_common.utils.L;
import com.misakanetwork.lib_common.utils.SingleToastUtils;
import com.misakanetwork.lib_common.utils.StatusBarUtils;
import com.misakanetwork.lib_common.utils.glide.GlideEngine;
import com.misakanetwork.lib_common.utils.glide.LoadImageUtils;
import com.misakanetwork.lib_common.utils.xutils.DownloadInfo;
import com.misakanetwork.lib_common.utils.xutils.DownloadManager;
import com.misakanetwork.lib_common.utils.xutils.DownloadState;
import com.misakanetwork.mvpprojectstructure.MainApplication;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.adapter.DownloadListTestAdapter;
import com.misakanetwork.mvpprojectstructure.helper.AppHelper;
import com.misakanetwork.mvpprojectstructure.helper.KeywordsHelper;
import com.misakanetwork.mvpprojectstructure.helper.PermissionContract;
import com.misakanetwork.mvpprojectstructure.mvp.contract.MainTestContract;
import com.misakanetwork.mvpprojectstructure.mvp.presenter.MainTestPresenterImpl;
import com.misakanetwork.mvpprojectstructure.ui.activity.base.BaseMVPActivity;
import com.misakanetwork.mvpprojectstructure.ui.dialog.AssignmentDialog;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class MainTestActivity extends BaseMVPActivity<MainTestPresenterImpl> implements MainTestContract.View {
    @BindView(R.id.titleTb)
    Toolbar titleTb;
    @BindView(R.id.butter_knife_tv)
    TextView butterKnifeTv;
    @BindView(R.id.status_bar_v)
    View statusBarV;
    @BindView(R.id.img_iv)
    ImageView imgIv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading_layout)
    ConstraintLayout loadingLayout;

    public static void startThis(Context context) {
        context.startActivity(new Intent(context.getApplicationContext(), MainTestActivity.class));
    }

    private RxPermissions rxPermissions;
    private long exitTime;
    private AssignmentDialog assignmentDialog;
    private DownloadListTestAdapter mAdapter;

    @Override
    protected MainTestPresenterImpl createPresenter() {
        return new MainTestPresenterImpl(this);
    }

    @Override
    protected void netOverride() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_test;
    }

    @Override
    protected boolean isImmerse() {
        return true;
    }

    @Override
    protected void initView(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        StatusBarUtils.initHeight(this, statusBarV);
        int isFirst = BaseAppHelper.getIsFirstRun();
        if (isFirst == 0) {
            showAssignment();
        } else {
            startInitView();
        }
    }

    private void startInitView() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (AppHelper.getIsNightMode() && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            setNightMode();
        }
        SingleToastUtils.init(this).showNormalApp("butterknife");
        SingleToastUtils.init(this).showNormalApp("butterknifeaaa");
        mPresenter.pCheckVersion();
        mAdapter = new DownloadListTestAdapter(this, new ArrayList<>());
        LinearLayoutManager manager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        DownloadManager.getInstance().setOnDownloadInfoChangedListener(new DownloadManager.OnDownloadInfoChangedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(DownloadInfo downloadInfo) {
                if (downloadInfo != null) {
                    if (downloadInfo.getState() == DownloadState.FINISHED && FileUtils.fileIsExists(downloadInfo.getFileSavePath())) {
                        showCenterToast(getString(R.string.string_download_success, downloadInfo.getFileSavePath()));
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        initToolbar(titleTb, 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//        showCustomLoading(loadingLayout, "abhh", true);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showCustomLoading(loadingLayout, null, false);
//            }
//        }, 3000);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isCustomLoadingDispatchDisabled(loadingLayout)) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isCustomLoadingOnKeyDownDisabled(loadingLayout, keyCode)) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void initToolbar(Toolbar mToolbar, int iconResId, View.OnClickListener listener) {
        super.initToolbar(mToolbar, iconResId, listener);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_return_white);
    }

    @SuppressLint("NotifyDataSetChanged")
    @OnClick({R.id.butter_knife_tv, R.id.agent_web_tv, R.id.api_tv, R.id.add_photo_tv, R.id.take_picture_tv, R.id.download_apk_tv, R.id.scan_tv,
            R.id.add_download_tv})
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.butter_knife_tv:
                SingleToastUtils.init(this).showNormalApp("butterknife");
                Log.e("butterknife", ">>>");
//                setNightMode();
                List<String> imgList = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    imgList.add("https://img2.baidu.com/it/u=2102736929,2417598652&fm=26&fmt=auto&gp=0.jpg");
                }
                ImagePagerActivity.startImagePagerActivity(this, imgList, 1, null, false);
                break;
            case R.id.agent_web_tv:
                WebActivity.startThis(this);
                break;
            case R.id.api_tv:
                ApiSetActivity.startThis(this);
                break;
            case R.id.add_photo_tv:
                if (rxPermissions == null) {
                    rxPermissions = new RxPermissions(this);
                }
                rxPermissions.request(PermissionContract.PHOTO)
                        .subscribe(granted -> {
                            if (granted) {
                                PictureSelector.create(MainTestActivity.this)
                                        .openGallery(PictureMimeType.ofImage())
                                        .maxSelectNum(6)
                                        .isGif(false)
                                        .selectionMode(PictureConfig.MULTIPLE)
                                        .imageSpanCount(3)
                                        .previewImage(false)
                                        .isCamera(true)
                                        .enableCrop(false)
                                        .compress(true)
                                        .withAspectRatio(1, 1)
                                        .freeStyleCropEnabled(false)
                                        .circleDimmedLayer(false)
                                        .showCropFrame(false)
                                        .showCropGrid(false)
                                        .rotateEnabled(false)
                                        .scaleEnabled(true)
                                        .loadImageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
                                        .forResult(KeywordsHelper.ALBUM_REQUEST_CODE);
                            } else {
                                SingleToastUtils.init(this).showNormalApp(getString(R.string.string_accept_failed));
                            }
                        }, new io.reactivex.rxjava3.functions.Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                SingleToastUtils.init(MainTestActivity.this).showNormalApp(getString(R.string.string_accept_failed));
                            }
                        });
                break;
            case R.id.take_picture_tv:
                if (rxPermissions == null) {
                    rxPermissions = new RxPermissions(this);
                }
                rxPermissions.request(PermissionContract.PHOTO)
                        .subscribe(granted -> {
                            if (granted) {
                                PictureSelector.create(MainTestActivity.this)
                                        .openCamera(PictureMimeType.ofImage())
                                        .enableCrop(true) // 是否裁剪
                                        .compress(true) // 压缩
                                        .minimumCompressSize(500)
                                        .withAspectRatio(1, 1)
                                        .freeStyleCropEnabled(false)
                                        .circleDimmedLayer(false)
                                        .showCropFrame(false)
                                        .showCropGrid(false)
                                        .rotateEnabled(false)
                                        .scaleEnabled(true)
                                        .forResult(KeywordsHelper.PHOTO_REQUEST_CODE);
                            } else {
                                SingleToastUtils.init(this).showNormalApp(getString(R.string.string_accept_failed));
                            }
                        }, new io.reactivex.rxjava3.functions.Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                SingleToastUtils.init(MainTestActivity.this).showNormal(getString(R.string.string_accept_failed));
                            }
                        });
                break;
            case R.id.download_apk_tv:
                VersionBean versionBean = new VersionBean();
                versionBean.setCode("36");
                versionBean.setFile("https://qyss.oss-cn-hangzhou.aliyuncs.com/app/android/teacher/qyzhjy_qyss_teacher.apk");
                showVersionDialog(versionBean, false);
                break;
            case R.id.scan_tv:
                if (rxPermissions == null) {
                    rxPermissions = new RxPermissions(this);
                }
                rxPermissions.request(PermissionContract.PHOTO)
                        .subscribe(granted -> {
                            if (granted) {
                                startActivityForResult(new Intent(this, CaptureActivity.class).putExtra("status", 1), KeywordsHelper.REQUEST_SCAN_QR_CODE_AUTH);
                            } else {
                                SingleToastUtils.init(this).showNormalApp(getString(R.string.string_accept_failed));
                            }
                        }, new io.reactivex.rxjava3.functions.Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                SingleToastUtils.init(MainTestActivity.this).showNormal(getString(R.string.string_accept_failed));
                            }
                        });
                break;
            case R.id.add_download_tv:
                String url = "https://qyss.oss-cn-hangzhou.aliyuncs.com/app/android/teacher/qyzhjy_qyss_teacher.apk";
                String label = "new added xUtils_" + System.nanoTime();
                try {
                    DownloadManager.getInstance().startDownload(
                            url, label,
                            BaseKeywordsHelper.getBoxSavePath(MainTestActivity.this) + "xUtilsDownload/" + label + ".apk",
                            true, false);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK &&
                (requestCode == KeywordsHelper.ALBUM_REQUEST_CODE || requestCode == KeywordsHelper.PHOTO_REQUEST_CODE)) {//拍照
            List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
            LoadImageUtils.loadImage(localMedia.get(0).getPath(), imgIv, false);
            List<MultipleFileInterface> picDataList = new ArrayList<>();
            for (LocalMedia media : localMedia) {
                picDataList.add(new LocalFileModel(media.getCompressPath()));
            }
            L.e(picDataList.get(0).getMultipleFileModel().getPath());
            mPresenter.pUpLoadField(picDataList);
        } else if (resultCode == RESULT_OK && requestCode == KeywordsHelper.REQUEST_SCAN_QR_CODE_AUTH && data != null) { // 扫码
            String content = data.getStringExtra(Constant.CODED_CONTENT);
            if (content != null) {
                showCenterToast(content);
            }
        }
    }

    @Override
    public void upLoadFields(List<NetFileModel> fileModels) {
        super.upLoadFields(fileModels);
        for (int i = 0; i < fileModels.size(); i++) {
            L.e("fileModels========>", fileModels.get(i).getSourcePath());
        }
        SingleToastUtils.init(this).showNormal("uploadSuccess!");
    }

    @Override
    public void upLoadFieldsFail(String failMsg) {
        super.upLoadFieldsFail(failMsg);
        SingleToastUtils.init(this).showNormal("uploadFailed!");
    }

    private void setNightMode() {
        //  获取当前模式
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //  将是否为夜间模式保存到SharedPreferences
        AppHelper.putIsNightMode(currentNightMode != Configuration.UI_MODE_NIGHT_YES);
        //  切换模式
        getDelegate().setDefaultNightMode(currentNightMode == Configuration.UI_MODE_NIGHT_NO ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
//        recreate();
        startActivity(new Intent(this, MainTestActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    /**
     * 协议弹窗
     */
    private void showAssignment() {
        if (assignmentDialog != null) {
            assignmentDialog.dismiss();
        }
        assignmentDialog = new AssignmentDialog();
        assignmentDialog.setOnClickListener(new AssignmentDialog.OnClickListener() {
            @Override
            public void onFirstClick() {

            }

            @Override
            public void onSecondClick() {

            }

            @Override
            public void onCancelClick() {
                System.exit(0);
            }

            @Override
            public void onCertainClick() {
                MainApplication.init(MainApplication.getInstance());
                BaseAppHelper.putIsFirstRun();
                startInitView();
                assignmentDialog.dismiss();
            }
        });
        assignmentDialog.showThis(getSupportFragmentManager(), AssignmentDialog.class.getSimpleName());
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            SingleToastUtils.init(this).showNormal(getString(R.string.string_exit_notice));
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}