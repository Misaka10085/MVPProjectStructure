package com.misakanetwork.mvpprojectstructure.helper;

import android.Manifest;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.mvpprojectstructure.helper
 * class name：PermissionContract
 * desc：
 */
public class PermissionContract {

    /*选择图片权限*/
    public static String[] PHOTO = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /*定位*/
    public static final String[] LOCAL = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    /*拨号*/
    public static final String[] CALL_PHONE = {Manifest.permission.CALL_PHONE};

    /*视频播放*/
    public static final String[] VIDEO_PLAY = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
}
