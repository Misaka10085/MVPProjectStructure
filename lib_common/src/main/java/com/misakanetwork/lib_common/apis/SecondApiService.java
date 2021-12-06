package com.misakanetwork.lib_common.apis;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static com.misakanetwork.lib_common.apis.Api.APP_SECOND_DOMAIN_NAME;
import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.apis
 * class name：SecondApiService
 * desc：SecondApiService
 */
public interface SecondApiService {
    @Headers({DOMAIN_NAME_HEADER + APP_SECOND_DOMAIN_NAME})
    @FormUrlEncoded
    @POST("/open/open-login")
    Observable<ResponseBody> opLogin(@FieldMap Map<String, Object> paramsMap);
}
