package com.misakanetwork.mvpprojectstructure.wxapi;

import java.util.List;

/**
 * Created By：Misaka10085
 * on：2021/8/11
 * package：com.misakanetwork.mvpprojectstructure.wxapi
 * class name：WxUserBean
 * desc：
 */
public class WxUserBean {

    /**
     * openid : oXC4Mw9twOJI2Z-S-jgSjnEucSH0
     * nickname : Misaka10085
     * sex : 1
     * language : zh_CN
     * city :
     * province :
     * country :
     * headimgurl : http://thirdwx.qlogo.cn/mmopen/vi_32/YE2XrwoM38vDhf8IjiaOgs8G1OXCRJ5qaIg7nTYHn9ZqGgibWTaG6faLpjU7iaTplpSrmZNw2ibSfWRgzPTrArrWtA/132
     * privilege : []
     * unionid : obrXYvnu36e0kIvMLB1J7sZd4NnE
     */

    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<?> privilege;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        if (nickname == null) {
            return "";
        } else {
            return nickname;
        }
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<?> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<?> privilege) {
        this.privilege = privilege;
    }
}

