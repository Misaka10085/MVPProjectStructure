package com.misakanetwork.lib_common.utils.xutils;

import com.misakanetwork.lib_common.utils.FileUtils;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.common.util.LogUtil;
import org.xutils.db.converter.ColumnConverterFactory;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * Created By：Misaka10085
 * on：2021/7/7
 * package：com.misakanetwork.lib_common.utils.xutils
 * class name：DownloadManager
 * desc：DownloadManager
 */
public final class DownloadManager {

    static {
        // 注册DownloadState在数据库中的值类型映射
        ColumnConverterFactory.registerColumnConverter(DownloadState.class, new DownloadStateConverter());
    }

    private static DownloadManager instance;

    private final static int MAX_DOWNLOAD_THREAD = 2; // 有效的值范围[1, 3], 设置为3时, 可能阻塞图片加载.

    private DbManager db;
    private final Executor executor = new PriorityExecutor(MAX_DOWNLOAD_THREAD, true);
    private final List<DownloadInfo> downloadInfoList = new ArrayList<DownloadInfo>();
    private final ConcurrentHashMap<DownloadInfo, DownloadCallback>
            callbackMap = new ConcurrentHashMap<DownloadInfo, DownloadCallback>(5);

    private OnDownloadInfoChangedListener onDownloadInfoChangedListener;

    public void setOnDownloadInfoChangedListener(OnDownloadInfoChangedListener onDownloadInfoChangedListener) {
        this.onDownloadInfoChangedListener = onDownloadInfoChangedListener;
    }

    private DownloadManager() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("download")
                .setDbVersion(1);
        try {
            db = x.getDb(daoConfig);
            List<DownloadInfo> infoList = db.selector(DownloadInfo.class).findAll();
            if (infoList != null) {
                for (DownloadInfo info : infoList) {
                    if (info.getState().value() < DownloadState.FINISHED.value()) {
                        info.setState(DownloadState.STOPPED);
                    }
                    downloadInfoList.add(info);
                }
            }
        } catch (DbException ex) {
            LogUtil.e(ex.getMessage(), ex);
        }
    }

    /*package*/
    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }

    public void updateDownloadInfo(DownloadInfo info) throws DbException {
        db.update(info);
        if (onDownloadInfoChangedListener != null) {
            onDownloadInfoChangedListener.onChanged(info);
        }
    }

    public int getDownloadListCount() {
        return downloadInfoList.size();
    }

    public DownloadInfo getDownloadInfo(int index) {
        return downloadInfoList.get(index);
    }

    public synchronized void startDownload(String url, String label, String savePath,
                                           boolean autoResume, boolean autoRename) throws DbException {

        String fileSavePath = new File(savePath).getAbsolutePath();
        DownloadInfo downloadInfo = db.selector(DownloadInfo.class)
                .where("label", "=", label)
                .and("fileSavePath", "=", fileSavePath)
                .findFirst();

        // create download info
        if (downloadInfo == null) {
            downloadInfo = new DownloadInfo();
            downloadInfo.setUrl(url);
            downloadInfo.setAutoRename(autoRename);
            downloadInfo.setAutoResume(autoResume);
            downloadInfo.setLabel(label);
            downloadInfo.setFileSavePath(fileSavePath);
            db.saveBindingId(downloadInfo);
        }

        // start downloading
        DownloadCallback callback = new DownloadCallback(downloadInfo);
        callback.setDownloadManager(this);
        RequestParams params = new RequestParams(url);
        params.setAutoResume(downloadInfo.isAutoResume());
        params.setAutoRename(downloadInfo.isAutoRename());
        params.setSaveFilePath(downloadInfo.getFileSavePath());
        params.setExecutor(executor);
        params.setCancelFast(true);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        callback.setCancelable(cancelable);
        callbackMap.put(downloadInfo, callback);

        if (downloadInfoList.contains(downloadInfo)) {
            int index = downloadInfoList.indexOf(downloadInfo);
            downloadInfoList.remove(downloadInfo);
            downloadInfoList.add(index, downloadInfo);
        } else {
            downloadInfoList.add(downloadInfo);
        }
        if (onDownloadInfoChangedListener != null) {
            onDownloadInfoChangedListener.onChanged(downloadInfo);
        }
    }

    public void stopDownload(int index) {
        DownloadInfo downloadInfo = downloadInfoList.get(index);
        stopDownload(downloadInfo);
    }

    public void stopDownload(DownloadInfo downloadInfo) {
        Callback.Cancelable cancelable = callbackMap.get(downloadInfo);
        if (cancelable != null) {
            cancelable.cancel();
        }
        if (onDownloadInfoChangedListener != null) {
            onDownloadInfoChangedListener.onChanged(downloadInfo);
        }
    }

    public void stopAllDownload() {
        for (DownloadInfo downloadInfo : downloadInfoList) {
            Callback.Cancelable cancelable = callbackMap.get(downloadInfo);
            if (cancelable != null) {
                cancelable.cancel();
            }
        }
        if (onDownloadInfoChangedListener != null) {
            onDownloadInfoChangedListener.onChanged(null);
        }
    }

    public void removeDownload(int index) throws DbException {
        DownloadInfo downloadInfo = downloadInfoList.get(index);
        db.delete(downloadInfo);
        stopDownload(downloadInfo);
        downloadInfoList.remove(index);
        if (FileUtils.fileIsExists(downloadInfo.getFileSavePath() + ".tmp")) {
            FileUtils.deleteDirWithFile(FileUtils.getFileFromPath(downloadInfo.getFileSavePath()));
        }
    }

    public void removeDownload(DownloadInfo downloadInfo) throws DbException {
        db.delete(downloadInfo);
        stopDownload(downloadInfo);
        downloadInfoList.remove(downloadInfo);
        if (FileUtils.fileIsExists(downloadInfo.getFileSavePath() + ".tmp")) {
            FileUtils.deleteDirWithFile(FileUtils.getFileFromPath(downloadInfo.getFileSavePath() + ".tmp"));
        }
    }

    public interface OnDownloadInfoChangedListener {
        void onChanged(DownloadInfo downloadInfo);
    }
}
