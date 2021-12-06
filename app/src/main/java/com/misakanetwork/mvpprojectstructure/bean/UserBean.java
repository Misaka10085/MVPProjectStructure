package com.misakanetwork.mvpprojectstructure.bean;

import java.io.Serializable;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.bean
 * class name：UserBean
 * desc：UserBean
 */
public class UserBean implements Serializable {

    /**
     * access_token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxMzQ2NzM0NDMyNzU2NzAzMjMzLCJ1c2VyX25hbWUiOiIxMzgwODYyNTc2NSIsInNjb3BlIjpbInNlcnZlciJdLCJleHAiOjE2MTI1MTM0OTMsImF1dGhvcml0aWVzIjpbIkdVRVNUIl0sImp0aSI6ImNkODgyN2FiLTNjMGQtNDdkYi05YzU5LTIyNTU5ZDRiMDg3ZSIsImNsaWVudF9pZCI6Im1iY2xvdWQiLCJ1c2VybmFtZSI6IjEzODA4NjI1NzY1In0.3Kc1kZGBn_10whfBkp57Rc5JGIAA27ltA4XVGwSIL-w
     * token_type : bearer
     * refresh_token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxMzQ2NzM0NDMyNzU2NzAzMjMzLCJ1c2VyX25hbWUiOiIxMzgwODYyNTc2NSIsInNjb3BlIjpbInNlcnZlciJdLCJhdGkiOiJjZDg4MjdhYi0zYzBkLTQ3ZGItOWM1OS0yMjU1OWQ0YjA4N2UiLCJleHAiOjE2MTI1MTM0OTMsImF1dGhvcml0aWVzIjpbIkdVRVNUIl0sImp0aSI6ImJiOTk3OGY1LWJmYjYtNGE0My05OTQxLTlmZjU3NGY4ZDEzMCIsImNsaWVudF9pZCI6Im1iY2xvdWQiLCJ1c2VybmFtZSI6IjEzODA4NjI1NzY1In0.oukJQUSZUML7b7yQh0Ze1SgMwg9lr-u29-YjLnZW-S4
     * expires_in : 2591999
     * scope : server
     * user_id : 1346734432756703233
     * username : 13808625765
     * jti : cd8827ab-3c0d-47db-9c59-22559d4b087e
     */

    private String access_token;
    private String token_type;
    private String refresh_token;
    private Integer expires_in;
    private String scope;
    private String id;
    private String username; // 手机号
    private String jti;
    private String background;
    private String avatar;
    private String autograph; // 个人简介
    private String birthday; // 生日
    private int gender; // 0:男，1：女
    private String name; // 用户名
    private String wxId; // 微信id
    private String qqId; // qqid
    private boolean push; // 是否打开推送
    private String wxName;
    private String qqName;

    public String getAccess_token() {
        return access_token == null ? "" : access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type == null ? "" : token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token == null ? "" : refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope == null ? "" : scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username == null ? "" : username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJti() {
        return jti == null ? "" : jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getBackground() {
        return background == null ? "" : background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getAvatar() {
        return avatar == null ? "" : avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAutograph() {
        return autograph == null ? "" : autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String getBirthday() {
        return birthday == null ? "" : birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getName() {
        String phone = getUsername().isEmpty() ?
                "" : getUsername().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return name == null ? phone : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWxId() {
        return wxId == null ? "" : wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getQqId() {
        return qqId == null ? "" : qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public String getWxName() {
        return wxName == null ? "" : wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName == null ? "" : wxName;
    }

    public String getQqName() {
        return qqName == null ? "" : qqName;
    }

    public void setQqName(String qqName) {
        this.qqName = qqName;
    }
}
