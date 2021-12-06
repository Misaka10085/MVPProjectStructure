package com.misakanetwork.mvpprojectstructure.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.misakanetwork.lib_common.adapter.CommonAdapter;
import com.misakanetwork.lib_common.adapter.CommonViewHolder;
import com.misakanetwork.lib_common.utils.FileUtils;
import com.misakanetwork.lib_common.utils.SingleToastUtils;
import com.misakanetwork.lib_common.utils.xutils.DownloadInfo;
import com.misakanetwork.lib_common.utils.xutils.DownloadManager;
import com.misakanetwork.lib_common.utils.xutils.DownloadState;
import com.misakanetwork.mvpprojectstructure.R;

import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created By：Misaka10085
 * on：2021/7/7
 * package：com.misakanetwork.mvpprojectstructure.adapter
 * class name：DownloadListTestAdapter
 * desc：DownloadListTestAdapter
 */
public class DownloadListTestAdapter extends CommonAdapter<String> {
    private DownloadManager downloadManager = DownloadManager.getInstance();

    public DownloadListTestAdapter(Context mContext, List<String> mData) {
        super(mContext, mData, R.layout.item_download_list_test);
    }

    @Override
    protected void bindData(CommonViewHolder holder, int position) {
        DownloadInfo downloadInfo = downloadManager.getDownloadInfo(position);
        holder.setText(R.id.download_label, downloadInfo.getLabel());
        DownloadState state = downloadInfo.getState();
        String stateStr;
        switch (state) {
            case WAITING:
            case STARTED:
                stateStr = mContext.getString(R.string.string_stop);
                break;
            case ERROR:
            case STOPPED:
                stateStr = mContext.getString(R.string.string_start_download);
                break;
            case FINISHED:
                stateStr = mContext.getString(R.string.string_start_download);
                break;
            default:
                stateStr = mContext.getString(R.string.string_start_download);
                break;
        }
        holder.setText(R.id.download_state, stateStr);
        Button stopBtn = holder.getView(R.id.download_stop_btn);
        stopBtn.setVisibility(downloadInfo.getState().equals(DownloadState.FINISHED) ? View.INVISIBLE : View.VISIBLE);
        stopBtn.setText(stateStr);
        Button removeFinishedBtn = holder.getView(R.id.download_remove_finished_btn);
        removeFinishedBtn.setVisibility(downloadInfo.getState().equals(DownloadState.FINISHED) ? View.VISIBLE : View.INVISIBLE);
        ProgressBar downloadPb = holder.getView(R.id.download_pb);
        downloadPb.setProgress(downloadInfo.getProgress());
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadState state = downloadInfo.getState();
                switch (state) {
                    case WAITING:
                    case STARTED:
                        downloadManager.stopDownload(downloadInfo);
                        break;
                    case ERROR:
                    case STOPPED:
                        try {
                            downloadManager.startDownload(
                                    downloadInfo.getUrl(),
                                    downloadInfo.getLabel(),
                                    downloadInfo.getFileSavePath(),
                                    downloadInfo.isAutoResume(),
                                    downloadInfo.isAutoRename());
                        } catch (DbException ex) {
                            SingleToastUtils.init(mContext).showNormal(mContext.getString(R.string.string_add_download_failed));
                        }
                        break;
                    case FINISHED:
                        SingleToastUtils.init(mContext).showNormal(mContext.getString(R.string.string_add_download_completed));
                        break;
                    default:
                        break;
                }
            }
        });
        removeFinishedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.deleteDirWithFile(FileUtils.getFileFromPath(downloadInfo.getFileSavePath()));
                SingleToastUtils.init(mContext).showNormal(mContext.getString(R.string.string_download_deleted));
                try {
                    downloadManager.removeDownload(downloadInfo);
                    notifyDataSetChanged();
                } catch (DbException e) {
                    SingleToastUtils.init(mContext).showNormal(mContext.getString(R.string.string_remove_task_failed));
                }
            }
        });
        holder.getView(R.id.download_remove_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    downloadManager.removeDownload(downloadInfo);
                    notifyDataSetChanged();
                } catch (DbException e) {
                    SingleToastUtils.init(mContext).showNormal(mContext.getString(R.string.string_remove_task_failed));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (downloadManager == null) return 0;
        return downloadManager.getDownloadListCount();
    }
}
