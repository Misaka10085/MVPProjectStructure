package com.misakanetwork.lib_common.mvp;

import com.misakanetwork.lib_common.apis.Api;
import com.misakanetwork.lib_common.entity.MultipleFileInterface;
import com.misakanetwork.lib_common.net.NetManager;
import com.misakanetwork.lib_common.utils.rx.RxHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.mvp
 * class name：BaseModelImpl
 * desc：BaseModelImpl
 */
public class BaseModelImpl implements BaseContract.BaseModel {

    @Override
    public void mUpLoadField(Observer<ResponseBody> observer, List<MultipleFileInterface> fileList) {
        HttpUrl httpUrl = RetrofitUrlManager.getInstance().fetchDomain(Api.AREA_FILE_DEFAULT_NAME);
        List<RequestBody> body = new ArrayList<>();
        List<RequestBody> descriptions = new ArrayList<>();
        for (MultipleFileInterface fieldBean : fileList) {
            if (fieldBean.getMultipleFileModel().isLocal()) {
                File file = new File(fieldBean.getMultipleFileModel().getPath());
                RequestBody requestField = RequestBody.create(MediaType.parse("image/*"), file);
                body.add(requestField);
            }
        }
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), "images");
        descriptions.add(description);

        String url = httpUrl + "image/upload/images";
        NetManager
                .getInstance()
                .getBaseApiService()
                .upLoadFields(url, body, descriptions)
                .compose(RxHelper.<ResponseBody>rxSchedulerHelper())
                .subscribe(observer);
    }

    @Override
    public void mCheckVersion(Observer<ResponseBody> observer) {
        NetManager.getInstance()
                .getBaseApiService()
                .getByVersion()
                .compose(RxHelper.<ResponseBody>rxSchedulerHelper())
                .subscribe(observer);
    }
}
