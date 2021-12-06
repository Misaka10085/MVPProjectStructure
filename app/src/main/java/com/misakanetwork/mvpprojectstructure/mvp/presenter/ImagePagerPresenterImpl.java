package com.misakanetwork.mvpprojectstructure.mvp.presenter;

import com.misakanetwork.lib_common.mvp.BasePresenterImpl;
import com.misakanetwork.mvpprojectstructure.mvp.contract.ImagePagerContract;
import com.misakanetwork.mvpprojectstructure.mvp.model.ImagePagerModelImpl;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.mvp.presenter
 * class name：ImagePagerPresenterImpl
 * desc：ImagePagerPresenterImpl
 */
public class ImagePagerPresenterImpl extends BasePresenterImpl<ImagePagerContract.View, ImagePagerContract.Model>
        implements ImagePagerContract.Presenter {

    public ImagePagerPresenterImpl(ImagePagerContract.View mView) {
        super(mView);
    }

    @Override
    protected ImagePagerContract.Model createModel() {
        return new ImagePagerModelImpl();
    }
}
