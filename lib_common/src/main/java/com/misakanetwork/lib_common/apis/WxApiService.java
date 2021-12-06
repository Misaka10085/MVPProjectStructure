package com.misakanetwork.lib_common.apis;

import static com.misakanetwork.lib_common.apis.Api.WX_DOMAIN_NAME;
import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

/**
 * Created By：Misaka10085
 * on：2021/8/11
 * package：com.misakanetwork.lib_common.apis
 * class name：WxApiService
 * desc：wxApi
 */
public interface WxApiService {

    /**
     * 获取微信AccessToken
     *
     * @param map appid
     *            secret
     *            code SendAuth.Resp
     *            grant_type authorization_code
     */
    @Headers({DOMAIN_NAME_HEADER + WX_DOMAIN_NAME})
    @GET("/oauth2/access_token")
    Observable<ResponseBody> wxAccessToken(@QueryMap Map<String, Object> map);

    /**
     * 微信获取个人信息
     *
     * @param map access_token
     *            openid
     */
    @Headers({DOMAIN_NAME_HEADER + WX_DOMAIN_NAME})
    @GET("/userinfo")
    Observable<ResponseBody> wxUserinfo(@QueryMap Map<String, Object> map);
}
