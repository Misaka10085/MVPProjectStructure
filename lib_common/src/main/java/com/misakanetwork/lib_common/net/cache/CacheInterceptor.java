package com.misakanetwork.lib_common.net.cache;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.net.cache
 * class name：CacheInterceptor
 * desc：CacheInterceptor
 */
public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        return null;
    }
}
