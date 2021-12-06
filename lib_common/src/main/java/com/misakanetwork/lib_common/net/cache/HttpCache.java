package com.misakanetwork.lib_common.net.cache;

import com.misakanetwork.lib_common.utils.ApplicationInstanceUtils;

import java.io.File;

import okhttp3.Cache;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.net.cache
 * class name：HttpCache
 * desc：HttpCache
 */
public class HttpCache {
    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 50 * 1024 * 1024;

    public static Cache getCache() {
        return new Cache(new File(ApplicationInstanceUtils.getInstance().getContext().getExternalCacheDir().getAbsolutePath() + File
                .separator + "data/NetCache"),
                HTTP_RESPONSE_DISK_CACHE_MAX_SIZE);
    }
}
