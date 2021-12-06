package com.misakanetwork.lib_common.mvp;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.misakanetwork.lib_common.entity.BaseObjectModel;
import com.misakanetwork.lib_common.entity.MultipleFileInterface;
import com.misakanetwork.lib_common.entity.NetFileModel;
import com.misakanetwork.lib_common.entity.VersionBean;
import com.misakanetwork.lib_common.utils.L;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.mvp
 * class name：BasePresenterImpl
 * desc：BasePresenterImpl
 */
public abstract class BasePresenterImpl<V extends BaseContract.BaseView, M extends BaseContract.BaseModel>
        implements BaseContract.BasePresenter {

    protected V mView;

    protected M mModel = createModel();

    protected abstract M createModel();

    public BasePresenterImpl(V mView) {
        this.mView = mView;
    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void release() {
    }

    /**
     * 解绑
     */
    @Override
    public void detachView() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.isDisposed();
        }
    }

    /**
     * 版本检测
     */
    @Override
    public void pCheckVersion() {
        mModel.mCheckVersion(new CommonObserver<BaseObjectModel<VersionBean>>(new TypeToken<BaseObjectModel<VersionBean>>() {
        }.getType()) {
            @Override
            protected void onSuccess(BaseObjectModel<VersionBean> beanBaseObjectModel) {
                mView.onVersionChecked(beanBaseObjectModel.body);
            }

            @Override
            public void onResultError(int code, String msg) {
                mView.doPrompt(msg);
            }
        });
    }

    /**
     * 上传文件
     */
    @Override
    public void pUpLoadField(List<MultipleFileInterface> fieldList) {
        mView.showLoading(true, 0f, false, false, "");
        mModel.mUpLoadField(new FileObserver() {
            @Override
            protected void onSuccess(List<NetFileModel> fieldBeans) {
                mView.hideLoading();
                mView.upLoadFields(fieldBeans);
            }

            @Override
            public void onResultError(int status, String msg) {
                mView.upLoadFieldsFail(msg);
                mView.hideLoading();
            }
        }, fieldList);
    }

    /**
     * 文件上传观察者
     */
    public abstract class FileObserver implements Observer<ResponseBody> {
        private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        @Override
        public void onComplete() {

        }

        @Override
        public void onSubscribe(Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(ResponseBody body) {
            try {
                if (body == null) {
                    onResultError(0, "服务器异常");
                    return;
                }
                String res = body.string();
                JSONTokener jsonParser = new JSONTokener(res);
                JSONObject root = (JSONObject) jsonParser.nextValue();
                int status = 0;
                String msg = "";
                String data = "";
                if (root.has("error_code")) {
                    status = root.getInt("error_code");
                }
                if (root.has("msg")) {
                    msg = root.getString("msg");
                }
                if (root.has("data")) {
                    data = root.getString("data");
                } else if (root.has("body")) {
                    data = root.getString("body");
                }
                if (status == 0) {
                    List<NetFileModel> fileModels = gson.fromJson(data, new TypeToken<List<NetFileModel>>() {
                    }.getType());
                    onSuccess(fileModels);
                } else {
                    onResultError(status, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                L.e("FileObserver", "异常->" + e.getMessage());
            }

        }

        @Override
        public void onError(Throwable e) {
            mView.onNetError(e.getMessage());
        }

        protected abstract void onSuccess(List<NetFileModel> fileModels);

        abstract public void onResultError(int status, String msg);
    }


    /**
     * 常用Observer（观察者）
     *
     * @param <T>
     */
    public abstract class CommonObserver<T> implements Observer<ResponseBody> {
        private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        private Type type;

        public CommonObserver(Type type) {
            this.type = type;
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onSubscribe(Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(ResponseBody body) {
            int code = 0;
            String msg = "";
            try {
                if (body == null) {
                    onResultError(0, "服务器异常");
                    return;
                }
                String res = body.string();
                JSONTokener jsonParser = new JSONTokener(res);
                JSONObject root = (JSONObject) jsonParser.nextValue();
                String data = "";
                if (root.has("code")) {
                    code = root.getInt("code");
                }
                if (root.has("msg")) {
                    msg = root.getString("msg");
                }
                if (root.has("data")) {
                    data = root.getString("data");
                } else if (root.has("body")) {
                    data = root.getString("body");
                }
//                root.has("loginCode")
                T t;
                if (type.equals(String.class)) {
                    t = (T) data;
                } else {
                    if (data != null && !data.equals("")) {
                        try {
                            t = gson.fromJson(res, type);
                        } catch (Exception e) {
                            t = null;
                            onResultError(code, e.getMessage());
                        }
                    } else {
                        t = null;
                    }
                }
                switch (code) {
                    case 0: // app内的成功
                    case 200:
                        onSuccess(t);
                        break;
                    case 401:
                        mView.onNoLogin(msg);
                        break;
                    default:
                        // view.doPrompt(msg);
                        onResultError(code, msg);
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                mView.onNetError(e.getMessage());
                Log.e("CommonObserver", "数据异常->" + e.getMessage());
            }
        }

        @Override
        public void onError(Throwable e) {
            String result = e.getMessage() == null ? "当前网络异常" : e.getMessage();
            if (result.contains("401")) {
                mView.onNoLogin("登录过期，请重新登陆");
                Log.e("CommonObserver", "异常信息->" + e.getMessage());
                return;
            } else {
                result = getErrStr(result);
                mView.onNetError(result);
            }
            Log.e("CommonObserver", "异常信息->" + e.getMessage());
        }

        protected abstract void onSuccess(T t);

        abstract public void onResultError(int code, String msg);
    }

    /**
     * 微信observer
     */
    public abstract class WechatObserver<T> implements Observer<ResponseBody> {
        private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        private Type type;

        public WechatObserver(Type type) {
            this.type = type;
        }

        @Override
        public void onComplete() {
        }

        @Override
        public void onSubscribe(Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(ResponseBody responseBody) {
            String string = null;
            try {
                string = responseBody.string();
                if (TextUtils.isEmpty(string)) {
                    onResultError("参数错误");
                    return;
                }
                onSuccess(string);
            } catch (IOException e) {
                e.printStackTrace();
                onResultError(e.getMessage());
            }
        }

        protected abstract void onSuccess(String t);

        abstract public void onResultError(String error);

        @Override
        public void onError(Throwable e) {
            Log.e("WechatObserver", "异常信息->" + e.getMessage());
        }
    }

    @NotNull
    private String getErrStr(String result) {
        if (result.contains("503")) {
            result = "服务器维护中";
        } else if (result.contains("500")) {
            result = "服务器异常";
        } else if (result.contains("timeout") || result.contains("time out")) {
            result = "请求超时";
        } else if (result.contains("HTTP")) {
            result = "连接服务器失败，请稍后再试";
        } else {
            result = "当前网络异常";
        }
        return result;
    }
}
