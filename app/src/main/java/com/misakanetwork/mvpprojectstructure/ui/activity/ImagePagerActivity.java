package com.misakanetwork.mvpprojectstructure.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.misakanetwork.lib_common.utils.StatusBarUtils;
import com.misakanetwork.lib_common.utils.clickcheck.AntiShake;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.adapter.ImagePagerAdapter;
import com.misakanetwork.mvpprojectstructure.bean.ImageSize;
import com.misakanetwork.mvpprojectstructure.mvp.contract.ImagePagerContract;
import com.misakanetwork.mvpprojectstructure.mvp.presenter.ImagePagerPresenterImpl;
import com.misakanetwork.mvpprojectstructure.ui.activity.base.BaseMVPActivity;
import com.misakanetwork.mvpprojectstructure.widget.ImageViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.ui.activity
 * class name：ImagePagerActivity
 * desc：ImagePagerActivity
 */
public class ImagePagerActivity extends BaseMVPActivity<ImagePagerPresenterImpl> implements ImagePagerContract.View, ViewPager.OnPageChangeListener {
    @BindView(R.id.pager)
    ImageViewPager pager;
    @BindView(R.id.guideGroup)
    LinearLayout guideGroup;
    @BindView(R.id.status_bar_v)
    View statusBarV;
    @BindView(R.id.title_center_tv)
    TextView titleCenterTv;
    @BindView(R.id.title_right_tv)
    TextView titleRightTv;

    private List<View> guideViewList = new ArrayList<View>();
    public ImageSize imageSize;
    private int startPos;
    private ArrayList<String> imgUrls;
    private boolean isDelete;
    private boolean isSingle = false;
    private int mPosition;
    private ImagePagerAdapter mAdapter;
    private boolean isLocal = false;

    public static final String INTENT_IMGURLS = "imgurls";
    public static final String INTENT_POSITION = "position";
    public static final String INTENT_IMAGESIZE = "imagesize";
    public static final String INTENT_ISDELETE = "isdelete";
    public static final String INTENT_ISSINGLE = "isSingle";
    public static final String INTENT_ISLOCAL = "isLocal";


    public static void startImagePagerActivity(Context context, List<String> imgUrls, int position, ImageSize imageSize, boolean isdelete) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putStringArrayListExtra(INTENT_IMGURLS, new ArrayList<String>(imgUrls));
        intent.putExtra(INTENT_POSITION, position);
        intent.putExtra(INTENT_IMAGESIZE, imageSize);
        intent.putExtra(INTENT_ISDELETE, isdelete);
        context.startActivity(intent);
    }

    public static void startImagePagerActivity(Context context, List<String> imgUrls, int position, ImageSize imageSize, boolean isdelete, boolean isSingle, boolean isLocal) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putStringArrayListExtra(INTENT_IMGURLS, new ArrayList<String>(imgUrls));
        intent.putExtra(INTENT_POSITION, position);
        intent.putExtra(INTENT_IMAGESIZE, imageSize);
        intent.putExtra(INTENT_ISDELETE, isdelete);
        intent.putExtra(INTENT_ISSINGLE, isSingle);
        intent.putExtra(INTENT_ISLOCAL, isLocal);
        context.startActivity(intent);
    }

    public static void startImagePagerActivity(Context context, List<String> imgUrls, int position, ImageSize imageSize, boolean isdelete, boolean isSingle) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putStringArrayListExtra(INTENT_IMGURLS, new ArrayList<String>(imgUrls));
        intent.putExtra(INTENT_POSITION, position);
        intent.putExtra(INTENT_IMAGESIZE, imageSize);
        intent.putExtra(INTENT_ISDELETE, isdelete);
        intent.putExtra(INTENT_ISSINGLE, isSingle);
        context.startActivity(intent);
    }

    @Override
    protected ImagePagerPresenterImpl createPresenter() {
        return new ImagePagerPresenterImpl(this);
    }

    @Override
    protected void netOverride() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_pager;
    }

    @Override
    protected boolean isImmerse() {
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        StatusBarUtils.initHeight(this, statusBarV);
        getIntentData();
        mAdapter = new ImagePagerAdapter(this, this);
        mAdapter.setDatas(imgUrls);
        mAdapter.setImageSize(imageSize);
        mAdapter.setIsFromGoods(isDelete);
        mAdapter.setIsLocal(isLocal);
        pager.setAdapter(mAdapter);
        pager.addOnPageChangeListener(this);
        pager.setCurrentItem(startPos);

        if (!isSingle) {
            titleCenterTv.setTextColor(getResources().getColor(R.color.color_white));
            titleCenterTv.setText((startPos + 1) + "/" + imgUrls.size());

            addGuideView(guideGroup, startPos, imgUrls);
        }

        if (isDelete) {
            titleRightTv.setTextColor(getResources().getColor(R.color.color_white));
            titleRightTv.setText(getString(R.string.string_delete));
        }

    }

    @OnClick({R.id.title_right_tv})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) return;
        switch (view.getId()) {
            case R.id.title_right_tv:
//        DeleteImageDialog deleteImageDialog = new DeleteImageDialog(ImagePagerActivity.this, getWindowManager());
//        deleteImageDialog.show();
//        deleteImageDialog.setClickListener(new DeleteImageDialog.ClickListenerInterface() {
//            @Override
//            public void onDialogClick(View view, int type) {
//                if (imgUrls.size() == 1) {
//                    imgUrls.remove(0);
//                    finish();
//                    ImageListBean imageListBean = new ImageListBean(imgUrls);
//                    RxBus.getInstance().post(imageListBean);
//                }else {
//                    if (mPosition + 1 == imgUrls.size()) {
//                        imgUrls.remove(mPosition);
//
//                        mAdapter.setDatas(imgUrls);
//                        mAdapter.setImageSize(imageSize);
//                        mAdapter.setIsFromGoods(isDelete);
//                        pager.setAdapter(mAdapter);
//                        pager.addOnPageChangeListener(ImagePagerActivity.this);
//                        pager.setCurrentItem(mPosition-1);
//                        addGuideView(guideGroup, mPosition-1, imgUrls);
//                        if (imgUrls.size() == 1) {
//                            headerView.setTitleTextColor(R.color.color_white);
//                            headerView.setTitleLabelText(1 + "/" + imgUrls.size());
//                        }
//                    }else {
//                        imgUrls.remove(mPosition);
//
//                        mAdapter.setDatas(imgUrls);
//                        mAdapter.setImageSize(imageSize);
//                        mAdapter.setIsFromGoods(isDelete);
//                        pager.setAdapter(mAdapter);
//                        pager.addOnPageChangeListener(ImagePagerActivity.this);
//                        pager.setCurrentItem(mPosition);
//                        addGuideView(guideGroup, mPosition, imgUrls);
//                        headerView.setTitleTextColor(R.color.color_white);
//                        headerView.setTitleLabelText((mPosition + 1) + "/" + imgUrls.size());
//                    }
//                }
//            }
//        });
                break;
        }
    }

    private void getIntentData() {
        startPos = getIntent().getIntExtra(INTENT_POSITION, 0);
        imgUrls = getIntent().getStringArrayListExtra(INTENT_IMGURLS);
        imageSize = (ImageSize) getIntent().getSerializableExtra(INTENT_IMAGESIZE);
        isDelete = getIntent().getBooleanExtra(INTENT_ISDELETE, false);
        isSingle = getIntent().getBooleanExtra(INTENT_ISSINGLE, false);
        isLocal = getIntent().getBooleanExtra(INTENT_ISLOCAL, false);
        mPosition = startPos;
    }

    private void addGuideView(LinearLayout guideGroup, int startPos, ArrayList<String> imgUrls) {
        if (imgUrls != null && imgUrls.size() > 0) {
            guideViewList.clear();
            for (int i = 0; i < imgUrls.size(); i++) {
                View view = new View(this);
                view.setBackgroundResource(R.drawable.gray_point);
                view.setSelected(i == startPos ? true : false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.gudieview_width),
                        getResources().getDimensionPixelSize(R.dimen.gudieview_heigh));
                layoutParams.setMargins(10, 0, 0, 0);
                guideGroup.addView(view, layoutParams);
                guideViewList.add(view);
            }
        }
        if (guideViewList != null && guideViewList.size() > 0) {
            for (int i = 0; i < guideViewList.size(); i++) {
                if (guideViewList.get(i).isSelected()) {
                    guideViewList.get(i).setBackgroundResource(R.drawable.white_point);
                } else {
                    guideViewList.get(i).setBackgroundResource(R.drawable.gray_point);
                }

            }
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        mPosition = position;
        if (!isSingle) {
            titleCenterTv.setTextColor(getResources().getColor(R.color.color_white));
            titleCenterTv.setText((position + 1) + "/" + imgUrls.size());

            for (int i = 0; i < guideViewList.size(); i++) {
                guideViewList.get(i).setSelected(i == position ? true : false);
                if (guideViewList.get(i).isSelected()) {
                    guideViewList.get(i).setBackgroundResource(R.drawable.white_point);
                } else {
                    guideViewList.get(i).setBackgroundResource(R.drawable.gray_point);
                }

            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
//            ImageListBean imageListBean = new ImageListBean(imgUrls);
//            RxBus.getInstance().post(imageListBean);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
