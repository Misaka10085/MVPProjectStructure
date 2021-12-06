package com.misakanetwork.mvpprojectstructure.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.misakanetwork.lib_common.adapter.CommonAdapter;
import com.misakanetwork.lib_common.adapter.CommonViewHolder;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.bean.ApiSetBean;

import java.util.List;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.adapter
 * class name：ApiSetAdapter
 * desc：ApiSetAdapter
 */
public class ApiSetAdapter extends CommonAdapter<ApiSetBean> {

    public ApiSetAdapter(Context mContext, List<ApiSetBean> mData) {
        super(mContext, mData, R.layout.item_api_set);
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    protected void bindData(CommonViewHolder holder, int position) {
        ApiSetBean bean = mData.get(position);
        holder.setText(R.id.type_name_tv, bean.getApiName());
        holder.setText(R.id.default_res_url_tv, mContext.getString(R.string.string_default_res_url, bean.getApiDefault()));
        EditText resGlobalUrlEdt = holder.getView(R.id.res_global_url_edt);
        resGlobalUrlEdt.setText(bean.getApiContent());
        resGlobalUrlEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = s.toString().trim();
                bean.setApiContent(content);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        holder.getView(R.id.default_res_btn_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener != null) {
                    resGlobalUrlEdt.setText(bean.getApiDefault());
                    bean.setApiContent(bean.getApiDefault());
                    resGlobalUrlEdt.setSelection(String.valueOf(resGlobalUrlEdt.getText()).length());
                    onItemClickedListener.onSelected(bean, position);
                }
            }
        });
    }

    public interface OnItemClickedListener {
        void onSelected(ApiSetBean apiSetBean, int position);
    }
}
