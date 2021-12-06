package com.misakanetwork.mvpprojectstructure;

import static com.misakanetwork.lib_common.apis.Api.APP_DEFAULT_DOMAIN;
import static com.misakanetwork.lib_common.apis.Api.APP_SECOND_DOMAIN;
import static com.misakanetwork.lib_common.apis.Api.APP_SECOND_DOMAIN_NAME;
import static com.misakanetwork.lib_common.apis.Api.AREA_FILE_DEFAULT_DOMAIN;
import static com.misakanetwork.lib_common.apis.Api.AREA_FILE_DEFAULT_NAME;
import static com.misakanetwork.lib_common.apis.Api.WX_DOMAIN;
import static com.misakanetwork.lib_common.apis.Api.WX_DOMAIN_NAME;
import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.misakanetwork.lib_common.apis.Api;
import com.misakanetwork.lib_common.global.GlobalApplication;
import com.misakanetwork.lib_common.helper.BaseAppHelper;
import com.misakanetwork.lib_common.utils.L;
import com.misakanetwork.lib_common.utils.SingleToastUtils;
import com.misakanetwork.lib_common.utils.xupdate.OKHttpUpdateHttpService;
import com.misakanetwork.lib_common.widget.gsyvideoplayer.GSYExoHttpDataSourceFactory;
import com.misakanetwork.mvpprojectstructure.bean.ApiSetBean;
import com.misakanetwork.mvpprojectstructure.helper.AppHelper;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;
import com.xuexiang.xupdate.utils.UpdateUtils;

import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import tv.danmaku.ijk.media.exo2.ExoMediaSourceInterceptListener;
import tv.danmaku.ijk.media.exo2.ExoSourceManager;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.mvpprojectstructure
 * class name：MainApplication
 * desc：Application
 */
@SuppressLint("StaticFieldLeak")
public class MainApplication extends GlobalApplication {
    public static MainApplication INSTANCE;
    public static Context context;
    public static boolean netBroken;

    public String imgConfig;

    public MainApplication() {
        INSTANCE = this;
    }

    public static MainApplication getInstance() {
        return INSTANCE;
    }

    public String getImgConfig() {
        return imgConfig;
    }

    public void setImgConfig(String imgConfig) {
        this.imgConfig = imgConfig;
    }

    public static boolean isNetBroken() {
        return netBroken;
    }

    public static void setNetBroken(boolean netBroken) {
        MainApplication.netBroken = netBroken;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        if (BaseAppHelper.getIsFirstRun() != 0) { // 同意协议后才进行初始化
            init(this);
        }
    }

    public static void init(Application application) {
        // 将每个 BaseUrl 进行初始化,运行时可以随时改变 DOMAIN_NAME 对应的值,从而达到切换 BaseUrl 的效果
        RetrofitUrlManager.getInstance().setDebug(true);
        RetrofitUrlManager.getInstance().setGlobalDomain(APP_DEFAULT_DOMAIN);
        RetrofitUrlManager.getInstance().putDomain(AREA_FILE_DEFAULT_NAME, AREA_FILE_DEFAULT_DOMAIN);
        RetrofitUrlManager.getInstance().putDomain(APP_SECOND_DOMAIN_NAME, APP_SECOND_DOMAIN);
        RetrofitUrlManager.getInstance().putDomain(WX_DOMAIN_NAME, WX_DOMAIN);
        List<ApiSetBean> mDomainData = new ArrayList<>();
        mDomainData.add(new ApiSetBean("资源服务器地址",
                Api.AREA_FILE_DEFAULT_NAME,
                AppHelper.getDomain().size() >= 1 ? AppHelper.getDomain().get(0).getApiContent() : Api.AREA_FILE_DEFAULT_DOMAIN,
                Api.AREA_FILE_DEFAULT_DOMAIN));
        mDomainData.add(new ApiSetBean("服务器地址1",
                Api.APP_DEFAULT_DOMAIN,
                AppHelper.getDomain().size() >= 2 ? AppHelper.getDomain().get(1).getApiContent() : Api.APP_DEFAULT_DOMAIN,
                Api.APP_DEFAULT_DOMAIN));
        mDomainData.add(new ApiSetBean("服务器地址2",
                Api.APP_SECOND_DOMAIN_NAME,
                AppHelper.getDomain().size() >= 3 ? AppHelper.getDomain().get(2).getApiContent() : Api.APP_SECOND_DOMAIN,
                Api.APP_SECOND_DOMAIN));
        AppHelper.putDomain(mDomainData);
        // End
        // XUpdate初始化
        XUpdate.get()
                .debug(false)
                .isWifiOnly(false)                                               // 默认设置只在wifi下检查版本更新
                .isGet(true)                                                    // 默认设置使用get请求检查版本
                .isAutoMode(false)                                              // 默认设置非自动模式，可根据具体使用配置
                .param("versionCode", UpdateUtils.getVersionCode(application))         // 设置默认公共请求参数
                .param("appKey", application.getPackageName())
                .setOnUpdateFailureListener(new OnUpdateFailureListener() {     // 设置版本更新出错的监听
                    @Override
                    public void onFailure(UpdateError error) {
                        if (error.getCode() != CHECK_NO_NEW_VERSION) {          // 对不同错误进行处理
                            SingleToastUtils.init(application).showNormalApp("XUpdate failed:" + error.toString());
                        }
                    }
                })
                .supportSilentInstall(true)                                     // 设置是否支持静默安装，默认是true
                .setIUpdateHttpService(new OKHttpUpdateHttpService())           // 这个必须设置！实现网络请求功能。
                .init(application);
        BaseAppHelper.putLaterUpdate(false); // 重置选择稍后更新状态
        // End
        // xUtils
        x.Ext.init(application);
        // End
        // GSYVideoPlayer
        ExoSourceManager.setExoMediaSourceInterceptListener(new ExoMediaSourceInterceptListener() {
            @Override
            public MediaSource getMediaSource(String dataSource, boolean preview, boolean cacheEnable, boolean isLooping, File cacheDir) {
                //如果返回 null，就使用默认的
                return null;
            }

            /**
             * 通过自定义的 HttpDataSource ，可以设置自签证书或者忽略证书
             * demo 里的 GSYExoHttpDataSourceFactory 使用的是忽略证书
             * */
            @Override
            public HttpDataSource.BaseFactory getHttpDataSourceFactory(String userAgent, @Nullable TransferListener listener, int connectTimeoutMillis, int readTimeoutMillis, boolean allowCrossProtocolRedirects) {
                //如果返回 null，就使用默认的
                return new GSYExoHttpDataSourceFactory(userAgent, listener,
                        connectTimeoutMillis,
                        readTimeoutMillis, allowCrossProtocolRedirects);
            }
        });
        L.e("ExoSourceManager device", ">>> " + android.os.Build.MANUFACTURER);
        // End
    }
}

