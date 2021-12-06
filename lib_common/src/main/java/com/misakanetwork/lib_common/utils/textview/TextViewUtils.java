package com.misakanetwork.lib_common.utils.textview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

/**
 * Created By：Misaka10085
 * on：2021/6/18
 * package：com.misakanetwork.lib_common.utils
 * class name：TextViewUtils
 * desc：TextViewUtils
 */
public class TextViewUtils {

    /**
     * 使用SpannableString设置样式——字体颜色
     */
    public static void setModeColor(String text, int color, int start, int end, TextView tv) {
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannableString.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(spannableString);
    }

    /**
     * 使用SpannableStringBuilder设置样式——字体大小
     */
    public static void setModeSize(String text, int size, int start, int end, TextView tv) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(text);
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(size);
        spannableString.setSpan(absoluteSizeSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(spannableString);
    }

    /**
     * 使用SpannableStringBuilder设置样式——删除线
     */
    public static void setModeDeleteLine(String text, int start, int end, TextView tv) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(text);
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannableString.setSpan(strikethroughSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(spannableString);
    }

    /**
     * 使用SpannableStringBuilder设置样式——下划线
     */
    public static void setModeUnderline(String text, int start, int end, TextView tv) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(text);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(underlineSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(spannableString);
    }

    /**
     * 使用SpannableStringBuilder设置样式——图片
     */
    public static void setModeImg(Context context, String text, int mipmap, int start, int end, TextView tv) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(text);
        ImageSpan imageSpan = new ImageSpan(context, mipmap);
        spannableString.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(spannableString);
    }

    /**
     * TextView末尾添加图片（跟随换行）
     */
    public static void endImage(Context mContext, TextView textView, String text, int drawableId, int marginStart) {
        textView.setText(text);
        Drawable drawable = mContext.getDrawable(drawableId);
        textView.post(new Runnable() {
            @Override
            public void run() {
                // 获取第一行的宽度
                float lineWidth = textView.getLayout().getLineWidth(0);
                // 获取第一行最后一个字符的下标
                int lineEnd = textView.getLayout().getLineEnd(0);
                // 计算每个字符占的宽度
                float widthPerChar = lineWidth / (lineEnd + 1);
                // 计算TextView一行能够放下多少个字符
                int numberPerLine = (int) Math.floor(textView.getWidth() / widthPerChar);
                // 在原始字符串中插入一个空格，插入的位置为numberPerLine - 1
//                StringBuilder stringBuilder = new StringBuilder(text).insert(numberPerLine - 1, " "); // 导致宽屏错误换行
                StringBuilder stringBuilder = new StringBuilder(text);
                //SpannableString的构建
                SpannableString spannableString = new SpannableString(stringBuilder.toString() + " ");

                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                spannableString.setSpan(new CenterSpaceImageSpan(drawable, marginStart, 0),
                        spannableString.length() - 1, spannableString.length(), ImageSpan.ALIGN_BASELINE
                );
                textView.setText(spannableString);
            }
        });
    }

    /**
     * TextView首部添加图片
     */
    public static void startImage(Context mContext, TextView textView, String text, int drawableId, int marginEnd) {
        textView.setText(text);
        Drawable drawable = mContext.getDrawable(drawableId);
        textView.post(new Runnable() {
            @Override
            public void run() {
                // 获取第一行的宽度
                float lineWidth = textView.getLayout().getLineWidth(0);
                // 获取第一行最后一个字符的下标
                int lineEnd = textView.getLayout().getLineEnd(0);
                // 计算每个字符占的宽度
                float widthPerChar = lineWidth / (lineEnd + 1);
                // 计算TextView一行能够放下多少个字符
                int numberPerLine = (int) Math.floor(textView.getWidth() / widthPerChar);
                // 在原始字符串中插入一个空格，插入的位置为numberPerLine - 1
                StringBuilder stringBuilder = new StringBuilder(text).insert(0, " ");
                // SpannableString的构建
                SpannableString spannableString = new SpannableString(" " + stringBuilder.toString());

                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                spannableString.setSpan(new CenterSpaceImageSpan(drawable, 0, marginEnd),
                        0, 1, ImageSpan.ALIGN_BASELINE
                );
                textView.setText(spannableString);
            }
        });
    }
}
