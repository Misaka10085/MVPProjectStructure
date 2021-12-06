package com.misakanetwork.lib_common.net;

import com.misakanetwork.lib_common.apis.Api;
import com.misakanetwork.lib_common.apis.BaseApiService;
import com.misakanetwork.lib_common.apis.SecondApiService;
import com.misakanetwork.lib_common.apis.WxApiService;
import com.misakanetwork.lib_common.net.cache.HttpCache;
import com.misakanetwork.lib_common.net.cookies.CookieManger;
import com.misakanetwork.lib_common.net.interceptor.LoggingInterceptor;
import com.misakanetwork.lib_common.net.interceptor.NetInterceptor;
import com.misakanetwork.lib_common.utils.ApplicationInstanceUtils;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.net
 * class name：NetManager
 * desc：NetManager
 */
public class NetManager {
    private static final int TIMEOUT_READ = 20;
    private static final int TIMEOUT_CONNECTION = 10;
    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;
    private BaseApiService baseApiService;
    private SecondApiService secondApiService;
    private WxApiService wxApiService;

    private static class NetWorkManagerHolder {
        private static final NetManager INSTANCE = new NetManager();
    }

    public static final NetManager getInstance() {
        return NetWorkManagerHolder.INSTANCE;
    }

    private NetManager() {
        SSLContext sslContext;
        final X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new X509TrustManager[]{trustManager}, new SecureRandom());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.mOkHttpClient = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder()) //RetrofitUrlManager 初始化
                .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                // 添加请求头
                .addInterceptor(new NetInterceptor())
                // 打印日志
                .addInterceptor(new LoggingInterceptor())
                // 设置Cache拦截器
                .cache(HttpCache.getCache())
                // 设置Cookie
                .cookieJar(new CookieManger(ApplicationInstanceUtils.getInstance().getContext()))
                // timeOut
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                // 失败重连
                .retryOnConnectionFailure(true)
                .build();

        this.mRetrofit = new Retrofit.Builder()
                .baseUrl(Api.APP_DEFAULT_DOMAIN)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 使用rxjava
                .addConverterFactory(GsonConverterFactory.create()) // 使用Gson
                .client(mOkHttpClient)
                .build();

        this.baseApiService = mRetrofit.create(BaseApiService.class);
        this.secondApiService = mRetrofit.create(SecondApiService.class);
        this.wxApiService = mRetrofit.create(WxApiService.class);
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public BaseApiService getBaseApiService() {
        return baseApiService;
    }

    public SecondApiService getOneApiService() {
        return secondApiService;
    }

    public WxApiService getWxApiService() {
        return wxApiService;
    }
}
