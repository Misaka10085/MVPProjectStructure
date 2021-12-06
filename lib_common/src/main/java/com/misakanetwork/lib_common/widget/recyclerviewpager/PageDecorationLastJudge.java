package com.misakanetwork.lib_common.widget.recyclerviewpager;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.lib_common.widget.recyclerviewpager
 * class name：PageDecorationLastJudge
 * desc：PageDecorationLastJudge
 */
public interface PageDecorationLastJudge {
    /**
     * Is the last row in one page
     */
    boolean isLastRow(int position);

    /**
     * Is the last Colum in one row;
     */
    boolean isLastColumn(int position);

    boolean isPageLast(int position);
}
