package com.misakanetwork.lib_common.apis;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.apis
 * class name：Api
 * desc：BaseApi
 */
public interface Api {
    // DEFAULT DOMAIN
    // api上方不配置注解@Headers({DOMAIN_NAME_HEADER + XXX_NAME})的接口，将仅受该baseUrl影响
    String APP_DEFAULT_DOMAIN = "https://admin.tlhappy.com/api/tule-api/";  // 线上
    // SECOND DOMAIN
    String APP_SECOND_DOMAIN_NAME = "TU_BANG_ZHU";
    String APP_SECOND_DOMAIN = "http://api5.tubangzhu.net/"; // 线上
    // WX DOMAIN
    String WX_DOMAIN_NAME = "WX_DOMAIN";
    String WX_DOMAIN = "https://api.weixin.qq.com/sns/";

    // Web
    String WEB_DEFAULT_DOMAIN = "http://www.baidu.com";

    // 资源服务器
    String AREA_FILE_DEFAULT_NAME = "AREA_FILE_DEFAULT";
    String AREA_FILE_DEFAULT_DOMAIN = "http://qyss.magic-beans.cn:9000/";

    // Oss配置
    String OSS_RESULT_PATH = "https://tulehudong.oss-cn-chengdu.aliyuncs.com/"; // 上传后返回结果
    String OSS_END_POINT = "oss-cn-chengdu.aliyuncs.com";
    String OSS_BUCKET_NAME = "tulehudong";
    String OSS_OBJECT_NAME = "images/appresources/";
    String OSS_ACCESS_KEY_ID = "LTAI4GL12DRqYh9vSi2e6M6G";
    String OSS_ACCESS_KEY_SECRET = "fcsOLaUPfwLH7WONODcRrLBKkfCM4k";
    String OSS_LIST = "?x-oss-process=image/resize,w_360,h_640"; // 列表图片略缩图拼接尾链
}
