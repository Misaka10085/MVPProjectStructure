package com.misakanetwork.mvpprojectstructure.mvp.presenter;

import com.misakanetwork.lib_common.mvp.BasePresenterImpl;
import com.misakanetwork.mvpprojectstructure.mvp.contract.MainTestContract;
import com.misakanetwork.mvpprojectstructure.mvp.model.MainTestModelImpl;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.mvp.presenter
 * class name：MainTestPresenterImpl
 * desc：MainTestPresenterImpl
 */
public class MainTestPresenterImpl extends BasePresenterImpl<MainTestContract.View, MainTestContract.Model>
        implements MainTestContract.Presenter {

    public MainTestPresenterImpl(MainTestContract.View mView) {
        super(mView);
    }

    @Override
    protected MainTestContract.Model createModel() {
        return new MainTestModelImpl();
    }
}
