package com.misakanetwork.lib_common.utils.xupdate;

import android.app.Activity;
import android.content.Context;

import com.misakanetwork.lib_common.R;
import com.misakanetwork.lib_common.helper.BaseAppHelper;
import com.misakanetwork.lib_common.helper.BaseKeywordsHelper;
import com.misakanetwork.lib_common.utils.FileUtils;
import com.misakanetwork.lib_common.utils.SingleToastUtils;
import com.misakanetwork.lib_common.utils.VersionHelper;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate._XUpdate;
import com.xuexiang.xupdate.service.OnFileDownloadListener;

import java.io.File;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.utils.xupdate
 * class name：UpdateUtils
 * desc：下载apk util
 */
public class UpdateUtils {
    public static void updateDownload(Activity activity, Context context, String url) {
        try {
            XUpdate.newBuild(context)
                    .apkCacheDir(BaseKeywordsHelper.getApkSavePath(context))
                    .build()
                    .download(url, new OnFileDownloadListener() {
                        @Override
                        public void onStart() {
                            HProgressDialogUtils.showHorizontalProgressDialog(activity, context, context.getString(R.string.string_update_progress), false);
                        }

                        @Override
                        public void onProgress(float progress, long total) {
                            HProgressDialogUtils.setProgress(Math.round(progress * 100));
                        }

                        @Override
                        public boolean onCompleted(File file) {
                            HProgressDialogUtils.cancel();
                            String filePath = file.getPath();
                            if (filePath.contains("unknown_version")) {
                                String oldFolderName = filePath.substring(0, filePath.indexOf("/" + file.getName()));
                                String newFolderName = "V" + VersionHelper.getVersionName(context);
                                filePath = FileUtils.fixFileName(oldFolderName, newFolderName) + "/" + file.getName();
                            }
                            filePath = FileUtils.fixFileName(filePath,
                                    VersionHelper.getAppName(context) + " v" + VersionHelper.getVersionName(context));
                            SingleToastUtils.init(context).showLongApp(context.getString(R.string.string_download_hint) + filePath);
                            _XUpdate.startInstallApk(context, new File(filePath));
                            BaseAppHelper.putApk(filePath);
                            return false;
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            HProgressDialogUtils.cancel();
                        }
                    });
        } catch (Exception e) {
            SingleToastUtils.init(context).showLongApp(e.getMessage());
        }
    }
}