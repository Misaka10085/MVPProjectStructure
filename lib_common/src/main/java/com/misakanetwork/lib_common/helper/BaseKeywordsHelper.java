package com.misakanetwork.lib_common.helper;

import android.content.Context;
import android.os.Environment;

import com.misakanetwork.lib_common.utils.FileUtils;
import com.misakanetwork.lib_common.utils.VersionHelper;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.helper
 * class name：KeywordsHelper
 * desc：KeywordsHelper
 */
public class BaseKeywordsHelper {

    public static String getApkSavePath(Context context) {
        String filePath = Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName() + "/apkdownload";
        if (!FileUtils.fileIsExists(filePath)) {
            FileUtils.createFolderFile(filePath);
        }
        return filePath;
    }

    public static String getApkSavedFolder(Context context) {
        return getApkSavePath(context) + "/V" + VersionHelper.getVersionName(context);
    }

    public static String getBoxSavePath(Context context) {
        String filePath = Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName() + "/MyData";
        if (!FileUtils.fileIsExists(filePath)) {
            FileUtils.createFolderFile(filePath);
        }
        return filePath;
    }

    public static String getImagesSavePath(Context context) {
        String filePath = getBoxSavePath(context) + "/MyPictures/";
        if (!FileUtils.fileIsExists(filePath)) {
            FileUtils.createFolderFile(filePath);
        }
        return filePath;
    }

    public static String getVideoSavePath(Context context) {
        String filePath = getBoxSavePath(context) + "/MyVideo/";
        if (!FileUtils.fileIsExists(filePath)) {
            FileUtils.createFolderFile(filePath);
        }
        return filePath;
    }
}
