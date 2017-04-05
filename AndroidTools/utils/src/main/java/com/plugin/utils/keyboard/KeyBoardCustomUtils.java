package com.plugin.utils.keyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.plugin.utils.R;

import java.util.List;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/4/1 18:46
 */

public class KeyBoardCustomUtils {
    private Context mContext;
    private Keyboard numberKeyBoard;// 数字键盘
    private Keyboard qwertyKeyBoard;// 字母键盘
    private KeyboardView mKeyboardView;// 键盘容器
    private OnCompleteListener mCompleteListener;
    public boolean isNum = false;// 是否数据键盘
    public boolean isUpper = false;// 是否大写
    private EditText mEditText;

    public KeyBoardCustomUtils(Context context, KeyboardView keyboardView) {
        this.mContext = context;
        this.mKeyboardView = keyboardView;
        numberKeyBoard = new Keyboard(context, R.xml.keyboard_num);
        qwertyKeyBoard = new Keyboard(context, R.xml.keyboard_qwerty);
        mKeyboardView.setKeyboard(numberKeyBoard);
        isNum = true;
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);// 是否上浮信息
        mKeyboardView.setOnKeyboardActionListener(listener);
    }

    public void setBindEditText(EditText editText) {
        mEditText = editText;
    }

    // 键盘动作监听
    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            switch (primaryCode) { //按键codes编码
                case Keyboard.KEYCODE_DELETE://删除
                    if (mEditText != null) {
                        int index = mEditText.getSelectionStart();
                        if (index > 0) {
                            Editable editable = mEditText.getText();
                            editable.delete(index - 1, index);
                        }
                    }
                    break;
                case Keyboard.KEYCODE_SHIFT: //大小写切换
                    changeKey();
                    mKeyboardView.setKeyboard(qwertyKeyBoard);
                    break;
                case Keyboard.KEYCODE_MODE_CHANGE:
                    changeKeyTONum();
                    break;
                case Keyboard.KEYCODE_CANCEL:
                    if (mEditText != null) {
                        mKeyboardView.setFocusable(true);
                        mKeyboardView.setFocusableInTouchMode(true);
                        mKeyboardView.requestFocus();
                    }
                    hideKeyboard();
                    break;
                default:
                    if (mEditText != null) {
                        int index = mEditText.getSelectionStart();
                        Editable editable = mEditText.getText();
                        editable.insert(index, Character.toString((char) primaryCode) + "");
                    }
                    break;
            }

        }

        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    /**
     * 切换键盘大小写字母
     * 按照ascii码表可知，小写字母 = 大写字母+32;
     */
    private void changeKey() {
        List<Keyboard.Key> keyList = qwertyKeyBoard.getKeys();
        if (isUpper) {//如果为真表示当前为大写，需切换为小写
            isUpper = false;
            for (Keyboard.Key key : keyList) {
                if (key.label != null && isWord(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                }
            }
        } else {//如果为假表示当前为小写，需切换为大写
            isUpper = true;
            for (Keyboard.Key key : keyList) {
                if (key.label != null && isWord(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0] - 32;
                }
            }
        }
    }


    /**
     * 数字键盘切换
     */
    private void changeKeyTONum() {
        if (isNum) { //当前为数字键盘,切换为全字母键盘
            isNum = false;
            mKeyboardView.setKeyboard(qwertyKeyBoard);
        } else {//当前为全字母键盘，切换为数字键盘
            isNum = true;
            mKeyboardView.setKeyboard(numberKeyBoard);
        }
    }

    /**
     * 判断是否为字母
     *
     * @param str 需判断的字符串
     */
    private boolean isWord(String str) {
        String wordStr = "abcdefghijklmnopqrstuvwxyz";
        if (wordStr.indexOf(str.toLowerCase()) > -1) {
            return true;
        }
        return false;
    }


    /**
     * 展示自定义软键盘
     */
    public void showKeyboard() {
        Log.e("zxl", "showKeyboard");
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mKeyboardView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏自定义软键盘
     */
    public void hideKeyboard() {
        Log.e("zxl", "hideKeyboard");
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            mKeyboardView.setVisibility(View.INVISIBLE);
        }
        mEditText = null;
    }

    /**
     * 监听完成
     */
    public void setOnCompleteListener(OnCompleteListener mCompleteListener) {
        this.mCompleteListener = mCompleteListener;
    }

    /**
     * 密码输入完成
     */
    public interface OnCompleteListener {
        void onPress(String str);
    }
}
