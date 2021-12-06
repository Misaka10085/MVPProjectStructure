package com.misakanetwork.lib_common.net.interceptor;

import com.misakanetwork.lib_common.helper.BaseAppHelper;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.net.interceptor
 * class name：NetInterceptor
 * desc：NetInterceptor
 */
public class NetInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request requestOld = chain.request();
        // 添加公共参数
        HttpUrl httpUrl = requestOld.url().newBuilder()
//                .addQueryParameter("userId", AppHelper.UserHelp.getAuthUserId())
                .build();

//        L.e("NetInterceptor","token->"+ SharedPrefUtil.open(SharedPrefUtil.NAME).getString("token"));
        String token = BaseAppHelper.getToken();
        if (token.isEmpty()) {
            token = "Basic bWJjbG91ZDptYmNsb3Vk";
        }
        // 添加公共请求头
        Request requestNew = requestOld.newBuilder()
//                .addHeader("mbcloud", "mbcloud")
                .addHeader("AppType", "JT")
                .addHeader("Authorization", token)
                .addHeader("DeviceType", "0")
                .addHeader("VersionCode", BaseAppHelper.getVersionName())
                .addHeader("DeviceModel", android.os.Build.MANUFACTURER)
                .url(httpUrl)
                .build();
        return chain.proceed(requestNew);
    }
}
