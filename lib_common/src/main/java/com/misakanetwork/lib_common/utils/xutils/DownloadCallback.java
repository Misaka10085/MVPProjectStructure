package com.misakanetwork.lib_common.utils.xutils;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created By：Misaka10085
 * on：2021/7/7
 * package：com.misakanetwork.lib_common.utils.xutils
 * class name：DownloadCallback
 * desc：DownloadCallback
 */
/*package*/ class DownloadCallback implements
        Callback.CommonCallback<File>,
        Callback.ProgressCallback<File>,
        Callback.Cancelable {

    private DownloadInfo downloadInfo;
    private DownloadManager downloadManager;
    private boolean cancelled = false;
    private Cancelable cancelable;

    public DownloadCallback(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    public void setDownloadManager(DownloadManager downloadManager) {
        this.downloadManager = downloadManager;
    }

    public void setCancelable(Cancelable cancelable) {
        this.cancelable = cancelable;
    }

    @Override
    public void onWaiting() {
        try {
            downloadInfo.setState(DownloadState.WAITING);
            downloadManager.updateDownloadInfo(downloadInfo);
        } catch (DbException ex) {
            LogUtil.e(ex.getMessage(), ex);
        }
    }

    @Override
    public void onStarted() {
        try {
            downloadInfo.setState(DownloadState.STARTED);
            downloadManager.updateDownloadInfo(downloadInfo);
        } catch (DbException ex) {
            LogUtil.e(ex.getMessage(), ex);
        }
    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {
        if (isDownloading) {
            try {
                downloadInfo.setState(DownloadState.STARTED);
                downloadInfo.setFileLength(total);
                downloadInfo.setProgress((int) (current * 100 / total));
                downloadManager.updateDownloadInfo(downloadInfo);
            } catch (DbException ex) {
                LogUtil.e(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void onSuccess(File result) {
        synchronized (DownloadCallback.class) {
            try {
                downloadInfo.setState(DownloadState.FINISHED);
                downloadManager.updateDownloadInfo(downloadInfo);
            } catch (DbException ex) {
                LogUtil.e(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        synchronized (DownloadCallback.class) {
            try {
                downloadInfo.setState(DownloadState.ERROR);
                downloadManager.updateDownloadInfo(downloadInfo);
            } catch (DbException e) {
                LogUtil.e(e.getMessage(), e);
            }
        }
    }

    @Override
    public void onCancelled(CancelledException cex) {
        synchronized (DownloadCallback.class) {
            try {
                downloadInfo.setState(DownloadState.STOPPED);
                downloadManager.updateDownloadInfo(downloadInfo);
            } catch (DbException ex) {
                LogUtil.e(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void onFinished() {
        cancelled = false;
    }

    private boolean isStopped() {
        DownloadState state = downloadInfo.getState();
        return isCancelled() || state.value() > DownloadState.STARTED.value();
    }

    @Override
    public void cancel() {
        cancelled = true;
        if (cancelable != null) {
            cancelable.cancel();
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
