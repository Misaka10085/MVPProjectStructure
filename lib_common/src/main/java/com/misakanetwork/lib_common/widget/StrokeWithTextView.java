package com.misakanetwork.lib_common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.misakanetwork.lib_common.R;
import com.misakanetwork.lib_common.utils.DensityUtils;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.lib_common.widget
 * class name：StrokeWithTextView
 * desc：
 */
public class StrokeWithTextView extends androidx.appcompat.widget.AppCompatTextView {
    private TextView outlineTextView = null;
    private int strokeColor = 0XFFFFFFFF;
    private float strokeWidth = 1;

    public StrokeWithTextView(Context context) {
        super(context);

        outlineTextView = new TextView(context);
        init();
    }

    public StrokeWithTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StrokeWithTextView);
        strokeColor = ta.getColor(R.styleable.StrokeWithTextView_text_stroke_color, 0XFFFFFFFF);
        strokeWidth = ta.getFloat(R.styleable.StrokeWithTextView_text_stroke_width, DensityUtils.dp2px(1));
        ta.recycle();
        outlineTextView = new TextView(context, attrs);
        init();
    }

    public StrokeWithTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView);
        strokeColor = ta.getColor(R.styleable.StrokeWithTextView_text_stroke_color, 0XFFFFFFFF);
        strokeWidth = ta.getFloat(R.styleable.StrokeWithTextView_text_stroke_width, DensityUtils.dp2px(1));
        ta.recycle();
        outlineTextView = new TextView(context, attrs, defStyle);
        init();
    }

    public void init() {
        TextPaint paint = outlineTextView.getPaint();
        paint.setStrokeWidth(DensityUtils.dp2px(strokeWidth)); //描边宽度
        paint.setStyle(Paint.Style.STROKE);
        outlineTextView.setTextColor(strokeColor); //描边颜色
        outlineTextView.setGravity(getGravity());
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        outlineTextView.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //设置轮廓文字
        CharSequence outlineText = outlineTextView.getText();
        if (outlineText == null || !outlineText.equals(this.getText())) {
            outlineTextView.setText(getText());
            postInvalidate();
        }
        outlineTextView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        outlineTextView.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        outlineTextView.draw(canvas);
        super.onDraw(canvas);
    }
//
//    private TextView borderText = null;///用于描边的TextView

//
//    public StrokeTextView(Context context) {
//        super(context);
//        borderText = new TextView(context);
//        init();
//    }
//
//    public StrokeTextView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        borderText = new TextView(context, attrs);
//        init();
//    }
//
//    public StrokeTextView(Context context, AttributeSet attrs,
//                          int defStyle) {
//        super(context, attrs, defStyle);
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView);
//        strokeColor = ta.getColor(R.styleable.StrokeTextView_text_stroke_color, 0XFFFFFFFF);
//        strokeWidth = ta.getDimension(R.styleable.StrokeTextView_text_stroke_width, 1);
//        borderText = new TextView(context, attrs, defStyle);
//        ta.recycle();
//        init();
//    }
//
//    public void init() {
//        TextPaint tp1 = borderText.getPaint();
//        tp1.setStrokeWidth(strokeWidth);                                  //设置描边宽度
//        tp1.setStyle(Paint.Style.STROKE);                             //对文字只描边
//        borderText.setTextColor(strokeColor);  //设置描边颜色
//        borderText.setGravity(getGravity());
//    }
//
//    @Override
//    public void setLayoutParams(ViewGroup.LayoutParams params) {
//        super.setLayoutParams(params);
//        borderText.setLayoutParams(params);
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        CharSequence tt = borderText.getText();
//
//        //两个TextView上的文字必须一致
//        if (tt == null || !tt.equals(this.getText())) {
//            borderText.setText(getText());
//            this.postInvalidate();
//        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        borderText.measure(widthMeasureSpec, heightMeasureSpec);
//    }
//
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        borderText.layout(left, top, right, bottom);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        borderText.draw(canvas);
//        super.onDraw(canvas);
//    }
}
