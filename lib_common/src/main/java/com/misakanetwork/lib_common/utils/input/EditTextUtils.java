package com.misakanetwork.lib_common.utils.input;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.EditText;

import com.misakanetwork.lib_common.R;
import com.misakanetwork.lib_common.utils.textview.CenterSpaceImageSpan;

/**
 * Created By：Misaka10085
 * on：2021/6/18
 * package：com.misakanetwork.lib_common.utils.input
 * class name：EditTextUtils
 * desc：EditTextUtils
 */
public class EditTextUtils {

    /**
     * EditText添加startHint icon
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public static void setDefaultHintStart(Context context, String content, int mipmap, EditText editText, int drawablePadding) {
        SpannableString defaultStr = new SpannableString(" " + content);
        Drawable writePenDraw = context.getDrawable(mipmap);
        writePenDraw.setBounds(0, 0, writePenDraw.getIntrinsicWidth(),
                writePenDraw.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(writePenDraw, ImageSpan.ALIGN_BASELINE);
        // 0与1是图标放置在defaultStr中的位置（开始位置与结束位置），在这里表示上述的空格
//        defaultStr.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        defaultStr.setSpan(new CenterSpaceImageSpan(writePenDraw, 0, drawablePadding),
                0, 1, ImageSpan.ALIGN_BASELINE
        );
        editText.setHint(defaultStr);
    }
}
