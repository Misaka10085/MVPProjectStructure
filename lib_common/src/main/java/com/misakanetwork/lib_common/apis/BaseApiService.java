package com.misakanetwork.lib_common.apis;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.apis
 * class name：BaseApiService
 * desc：BaseApiService
 */
public interface BaseApiService {

    /**
     * 多文件上传
     */
    @Headers({DOMAIN_NAME_HEADER + Api.AREA_FILE_DEFAULT_NAME})
    @Multipart
    @POST
    Observable<ResponseBody> upLoadFields(@Url String url,
                                          @Part("file\"; filename=\"file.jpg") List<RequestBody> file,
                                          @Part("path") List<RequestBody> path);

    /**
     * 获取最新版本信息
     */
    @GET("/app/version/checkVersion")
    Observable<ResponseBody> getByVersion();

    /**
     * 引导页
     *
     * @param type 类型 FIRST("首次引导页"),LOGIN("登录引导页")
     */
    @GET("/guidePage/list")
    Observable<ResponseBody> getGuidePageList(@Query("type") String type);
}
