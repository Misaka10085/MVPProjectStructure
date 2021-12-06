package com.misakanetwork.lib_common.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.misakanetwork.lib_common.R;


/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.mvpprojectstructure.widget
 * class name：AmountView
 * desc：购物加减计数器
 */
public class AmountView extends LinearLayout implements View.OnClickListener {
    private int afterDot;

    private EditText etAmount;

    private int num = 0;

    private int maxNum = 999999;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        etAmount.setText(String.valueOf(num));
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        if (num > maxNum) {
            etAmount.setText(String.valueOf(maxNum));
            if (mOnNumberChangeListener != null) {
                mOnNumberChangeListener.numChange(maxNum);
            }
        }
        this.maxNum = maxNum;
    }

    public AmountView(Context context) {
        super(context);

        init(context);
    }

    public AmountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AmountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.number_add_sub_view, this);
        findViewById(R.id.btnDecrease).setOnClickListener(this);
        findViewById(R.id.btnIncrease).setOnClickListener(this);
        etAmount = findViewById(R.id.etAmount);
        etAmount.setText(String.valueOf(num));
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null) {
                    num = 0;
                    etAmount.setText(String.valueOf(num));
                    if (mOnNumberChangeListener != null) {
                        mOnNumberChangeListener.numChange(num);
                    }
                    return;
                }
                if (s.toString().isEmpty()) {
                    num = 0;
                    etAmount.setText(String.valueOf(num));
                    if (mOnNumberChangeListener != null) {
                        mOnNumberChangeListener.numChange(num);
                    }
                    return;
                }
                String str = s.toString();
                try {
                    num = Integer.parseInt(str);
                    if (num > maxNum) {
                        num = maxNum;
                        etAmount.setText(String.valueOf(maxNum));
                        etAmount.selectAll();
                        if (mOnNumberChangeListener != null) {
                            mOnNumberChangeListener.numChange(num);
                        }
                        return;
                    }

                } catch (Exception e) {
                    num = 0;
                }
                if (mOnNumberChangeListener != null) {
                    mOnNumberChangeListener.numChange(num);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                judge(s);
                etAmount.setSelection(String.valueOf(etAmount.getText()).length());
            }
        });
        etAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etAmount.getText().toString();
                if (!text.isEmpty()) {
                    etAmount.selectAll();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnDecrease) {
            num -= 1;
            if (num < 0) {
                num = maxNum;
            }
            etAmount.setText(String.valueOf(num));
            if (mOnNumberChangeListener != null) {
                mOnNumberChangeListener.numChange(num);
            }
            etAmount.setSelection(String.valueOf(etAmount.getText()).length());
        } else if (id == R.id.btnIncrease) {
            num += 1;
            if (num > maxNum) {
                num = 0;
            }
            etAmount.setText(String.valueOf(num));
            if (mOnNumberChangeListener != null) {
                mOnNumberChangeListener.numChange(num);
            }
            etAmount.setSelection(String.valueOf(etAmount.getText()).length());
        }
    }

    private void judge(Editable editable) {
        String temp = editable.toString();
        //连续输入0
        if (temp.equals("00")) {
            editable.delete(1, 2);
            return;
        }
        //输入"08" 等类似情况
        if (temp.startsWith("0") && temp.length() > 1) {
            editable.delete(0, 1);
            return;
        }
    }

    private OnNumberChangeListener mOnNumberChangeListener;

    public OnNumberChangeListener getOnNumberChangeListener() {
        return mOnNumberChangeListener;
    }

    public void setOnNumberChangeListener(OnNumberChangeListener mOnNumberChangeListener) {
        this.mOnNumberChangeListener = mOnNumberChangeListener;
    }

    public interface OnNumberChangeListener {
        void numChange(int num);
    }
}
