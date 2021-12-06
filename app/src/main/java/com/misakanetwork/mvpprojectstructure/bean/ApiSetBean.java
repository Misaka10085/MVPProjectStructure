package com.misakanetwork.mvpprojectstructure.bean;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.bean
 * class name：ApiSetBean
 * desc：ApiSetBean
 */
public class ApiSetBean {
    private String apiName;
    private String apiOriginName;
    private String apiContent;
    private String apiDefault;

    public ApiSetBean(String apiName, String apiOriginName, String apiContent, String apiDefault) {
        this.apiName = apiName;
        this.apiOriginName = apiOriginName;
        this.apiContent = apiContent;
        this.apiDefault = apiDefault;
    }

    public String getApiName() {
        return apiName == null ? "" : apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiOriginName() {
        return apiOriginName == null ? "" : apiOriginName;
    }

    public void setApiOriginName(String apiOriginName) {
        this.apiOriginName = apiOriginName;
    }

    public String getApiContent() {
        return apiContent == null ? "" : apiContent;
    }

    public void setApiContent(String apiContent) {
        this.apiContent = apiContent;
    }

    public String getApiDefault() {
        return apiDefault == null ? "" : apiDefault;
    }

    public void setApiDefault(String apiDefault) {
        this.apiDefault = apiDefault;
    }
}
