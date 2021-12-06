package com.misakanetwork.lib_common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.adapter
 * class name：CommonAdapter
 * desc：CommonAdapter
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {
    protected String TAG = this.getClass().getSimpleName();
    protected LayoutInflater mInflater;
    protected List<T> mData;
    protected T mDataBean;
    protected Context mContext;
    private int mLayoutId = -1;
    protected MultipleType<T> mTypeSupport;

    public CommonAdapter(Context mContext, List<T> mData) {
        this.mData = mData;
        this.mLayoutId = -1;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public CommonAdapter(Context mContext, T mDataBean) {
        this.mDataBean = mDataBean;
        this.mLayoutId = -1;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public CommonAdapter(Context mContext, List<T> mData, int mLayoutId) {
        this.mData = mData;
        this.mLayoutId = mLayoutId;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public CommonAdapter(Context mContext, List<T> mData, MultipleType<T> mTypeSupport) {
        this.mData = mData;
        this.mLayoutId = mLayoutId;
        this.mContext = mContext;
        this.mTypeSupport = mTypeSupport;
        mInflater = LayoutInflater.from(mContext);
    }

    public void clearData() {
        mData.clear();
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mTypeSupport != null) {
            mLayoutId = viewType;
        }
        View view = mInflater.inflate(mLayoutId, parent, false);
        return new CommonViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (mTypeSupport != null) {
            return mTypeSupport.getLayoutId(mData.get(position), position);
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        bindData(holder, position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    protected abstract void bindData(CommonViewHolder holder, int position);
}