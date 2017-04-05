package com.zxl.androidtools.ui.customviews;

import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.widget.EditText;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.utils.keyboard.KeyBoardCustomUtils;
import com.plugin.utils.keyboard.KeyboardUtils;
import com.zxl.androidtools.R;

import butterknife.Bind;

/**
 * @Description: 自定义键盘
 * @Author: zxl
 * @Date: 2017/4/1 11:15
 */

public class KeyBoardActivity extends BaseAppCompatActivity {
    @Bind(R.id.et1)
    EditText et1;
    @Bind(R.id.et2)
    EditText et2;
    @Bind(R.id.keyboard_view)
    KeyboardView keyboardView;

    private KeyBoardCustomUtils keyboardUtil;// 软键盘类

    @Override
    public int getLayoutId() {
        return R.layout.activity_keyboard;
    }

    public void initView() {
        keyboardUtil = new KeyBoardCustomUtils(this,keyboardView);
        KeyboardUtils.getInstance().disableShowSoftInput(et1);
        KeyboardUtils.getInstance().disableShowSoftInput(et2);
        et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    keyboardUtil.setBindEditText(et1);
                    keyboardUtil.showKeyboard();
                }
            }
        });
        et2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    keyboardUtil.setBindEditText(et2);
                    keyboardUtil.showKeyboard();
                }
            }
        });
    }


}
