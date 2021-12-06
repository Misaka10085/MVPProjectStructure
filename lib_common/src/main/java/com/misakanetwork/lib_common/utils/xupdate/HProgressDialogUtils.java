package com.misakanetwork.lib_common.utils.xupdate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.misakanetwork.lib_common.widget.BaseProgressDialog;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.utils.xupdate
 * class name：HProgressDialogUtils
 * desc：XUpdate下载进度Dialog
 */
public class HProgressDialogUtils {
    private static ProgressDialog sHorizontalProgressDialog;

    private HProgressDialogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    @SuppressLint("NewApi")
    public static void showHorizontalProgressDialog(Activity activity, Context context, String msg, boolean isShowSize) {
        cancel();

        if (sHorizontalProgressDialog == null) {
            sHorizontalProgressDialog = new BaseProgressDialog(activity, context);
            sHorizontalProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            sHorizontalProgressDialog.setCancelable(false);
            if (isShowSize)
                sHorizontalProgressDialog.setProgressNumberFormat("%2dMB/%1dMB");

        }
        if (!TextUtils.isEmpty(msg)) {
            sHorizontalProgressDialog.setMessage(msg);
        }
        sHorizontalProgressDialog.show();

    }

    public static void setMax(long total) {
        if (sHorizontalProgressDialog != null) {
            sHorizontalProgressDialog.setMax(((int) total) / (1024 * 1024));
        }
    }

    public static void cancel() {
        if (sHorizontalProgressDialog != null) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }

    public static void setProgress(int current) {
        if (sHorizontalProgressDialog == null) {
            return;
        }
        sHorizontalProgressDialog.setProgress(current);
        if (sHorizontalProgressDialog.getProgress() >= sHorizontalProgressDialog.getMax()) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }

    public static void setProgress(long current) {
        if (sHorizontalProgressDialog == null) {
            return;
        }
        sHorizontalProgressDialog.setProgress(((int) current) / (1024 * 1024));
        if (sHorizontalProgressDialog.getProgress() >= sHorizontalProgressDialog.getMax()) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }

    public static void onLoading(long total, long current) {
        if (sHorizontalProgressDialog == null) {
            return;
        }
        if (current == 0) {
            sHorizontalProgressDialog.setMax(((int) total) / (1024 * 1024));
        }
        sHorizontalProgressDialog.setProgress(((int) current) / (1024 * 1024));
        if (sHorizontalProgressDialog.getProgress() >= sHorizontalProgressDialog.getMax()) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }
}
