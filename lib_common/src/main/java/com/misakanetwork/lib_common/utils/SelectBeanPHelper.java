package com.misakanetwork.lib_common.utils;

import com.misakanetwork.lib_common.entity.BaseSelectPBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By：Misaka10085
 * on：2021/6/18
 * package：com.misakanetwork.lib_common.utils
 * class name：SelectBeanPHelper
 * desc：实体类选中状态工具
 */
public class SelectBeanPHelper {

    /**
     * 全选、撤销全选选择实体列表
     */
    public static <T extends BaseSelectPBean> void selectAllSelectBeans(List<T> selectBeans, boolean select) {
        if (StrUtils.isEmpty(selectBeans)) {
            return;
        }
        for (BaseSelectPBean selectBean : selectBeans) {
            selectBean.setSelected(select);
        }
    }

    /**
     * 检查选择实体列表是否已经全选
     */
    public static <T extends BaseSelectPBean> boolean isSelectAll(List<T> selectBeans) {
        if (StrUtils.isEmpty(selectBeans)) {
            return false;
        }
        for (BaseSelectPBean selectBean : selectBeans) {
            if (!selectBean.isSelect()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查选择实体列表是否都没选
     */
    public static <T extends BaseSelectPBean> boolean isNotSelectAll(List<T> selectBeans) {
        if (StrUtils.isEmpty(selectBeans)) {
            return false;
        }
        for (BaseSelectPBean selectBean : selectBeans) {
            if (selectBean.isSelect()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取已被选择的选择实体（单选）
     */
    public static <T extends BaseSelectPBean> T getSelectBean(List<T> selectBeans) {
        if (StrUtils.isEmpty(selectBeans)) {
            return null;
        }
        for (T selectBean : selectBeans) {
            if (selectBean.isSelect()) {
                return selectBean;
            }
        }
        return null;
    }

    /**
     * 获取已被选择的选择实体（多选）
     */
    public static <T extends BaseSelectPBean> List<T> getSelectBeans(List<T> selectBeans) {
        ArrayList<T> ts = new ArrayList<>();
        if (!StrUtils.isEmpty(selectBeans)) {
            for (T selectBean : selectBeans) {
                if (selectBean.isSelect()) {
                    ts.add(selectBean);
                }
            }
        }
        return ts;
    }
}
