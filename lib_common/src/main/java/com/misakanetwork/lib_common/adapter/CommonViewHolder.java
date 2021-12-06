package com.misakanetwork.lib_common.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.misakanetwork.lib_common.widget.SingleLineZoomTextView;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.adapter
 * class name：CommonViewHolder
 * desc：CommonViewHolder
 */
public class CommonViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mView;

    public CommonViewHolder(View itemView) {
        super(itemView);
        mView = new SparseArray<>();
    }

    public <T extends View> T getView(int viewId) {
        View view = mView.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mView.put(viewId, view);
        }
        return (T) view;
    }

    public <T extends ViewGroup> T getViewGroup(int vieeId) {
        View view = mView.get(vieeId);
        if (view == null) {
            view = itemView.findViewById(vieeId);
            mView.put(vieeId, view);
        }
        return (T) view;
    }

    // 封装通用功能
    public CommonViewHolder setText(int viewId, CharSequence text) {
        if (getView(viewId) instanceof SingleLineZoomTextView) {
            SingleLineZoomTextView singleLineZoomTextView = getView(viewId);
            singleLineZoomTextView.setSingleLineText(String.valueOf(text));
        } else {
            TextView textView = getView(viewId);
            textView.setText(String.valueOf(text));
        }
        return this;
    }

    public CommonViewHolder setHintText(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setHint(String.valueOf(text));
        return this;
    }

    public CommonViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }
}

