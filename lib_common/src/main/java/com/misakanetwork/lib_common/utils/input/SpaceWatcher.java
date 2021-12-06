package com.misakanetwork.lib_common.utils.input;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created By：Misaka10085
 * on：2021/6/18
 * package：com.misakanetwork.lib_common.utils.input
 * class name：SpaceWatcher
 * desc：EditText空格限制
 */
public class SpaceWatcher implements TextWatcher {
    private EditText editText;

    public SpaceWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(" ")) {
            String[] str = s.toString().split(" ");
            StringBuilder content = new StringBuilder();
            for (String value : str) {
                content.append(value);
            }
            editText.setText(content.toString());
            editText.setSelection(start);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
