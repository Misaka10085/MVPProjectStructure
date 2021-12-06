package com.misakanetwork.lib_common.entity;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.entity
 * class name：BaseModel
 * desc：网络请求BaseModel
 */
public class BaseModel {
    // 网络请求返回code
    public Integer code;
    // 当code为失败时，服务器将返回错误提示
    public String msg;
}
