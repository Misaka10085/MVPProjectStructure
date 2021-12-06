package com.misakanetwork.lib_common.entity;

import java.io.Serializable;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.entity
 * class name：BaseSelectBean
 * desc：实体类选中状态bean
 */
public class BaseSelectSBean implements Serializable {
    protected boolean isSelected = false;

    public boolean isSelect() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
