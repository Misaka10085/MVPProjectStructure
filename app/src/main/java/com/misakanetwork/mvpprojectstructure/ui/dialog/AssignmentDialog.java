package com.misakanetwork.mvpprojectstructure.ui.dialog;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.misakanetwork.lib_common.utils.clickcheck.AntiShake;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.ui.dialog.base.BaseCenterDialog;
import com.ruffian.library.widget.RTextView;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.ui.dialog
 * class name：AssignmentDialog
 * desc：AssignmentDialog
 */
public class AssignmentDialog extends BaseCenterDialog {

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected int setViewId() {
        return R.layout.dialog_assignment;
    }

    @Override
    protected void initView(View view) {
        TextView contentTv2 = view.findViewById(R.id.content_tv2);
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder(contentTv2.getText().toString());
        ClickableSpan clickableSpanOne = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (!AntiShake.check(widget.getId()) && onClickListener != null) {
                    onClickListener.onFirstClick();
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(ds.linkColor);
                ds.setUnderlineText(false);
            }
        };
        spannableBuilder.setSpan(clickableSpanOne, 4, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpanTwo = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (!AntiShake.check(widget.getId()) && onClickListener != null) {
                    onClickListener.onSecondClick();
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(ds.linkColor);
                ds.setUnderlineText(false);
            }
        };
        spannableBuilder.setSpan(clickableSpanTwo, 11, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        contentTv2.setMovementMethod(LinkMovementMethod.getInstance());
        contentTv2.setText(spannableBuilder);
        RTextView cancelRtv = view.findViewById(R.id.cancelRtv);
        cancelRtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AntiShake.check(v.getId()) && onClickListener != null) {
                    onClickListener.onCancelClick();
                }
            }
        });
        RTextView sureRtv = view.findViewById(R.id.sureRtv);
        sureRtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AntiShake.check(v.getId()) && onClickListener != null) {
                    onClickListener.onCertainClick();
                }
            }
        });
    }

    public interface OnClickListener {
        void onFirstClick();

        void onSecondClick();

        void onCancelClick();

        void onCertainClick();
    }

    @Override
    protected void initBundle(Bundle bundle) {
    }

    @Override
    protected boolean clickBack() {
        return false;
    }

    @Override
    protected boolean setOutSide() {
        return false;
    }

    @Override
    protected float halfTpValue() {
        return 0.8f;
    }
}
