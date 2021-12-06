package com.misakanetwork.lib_common.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.entity
 * class name：BaseSelectPBean
 * desc：实体类选中状态bean
 */
public class BaseSelectPBean implements Parcelable {
    protected boolean isSelected = false;

    public BaseSelectPBean() {
    }

    public boolean isSelect() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    protected BaseSelectPBean(Parcel in) {
        isSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BaseSelectPBean> CREATOR = new Creator<BaseSelectPBean>() {
        @Override
        public BaseSelectPBean createFromParcel(Parcel in) {
            return new BaseSelectPBean(in);
        }

        @Override
        public BaseSelectPBean[] newArray(int size) {
            return new BaseSelectPBean[size];
        }
    };
}
