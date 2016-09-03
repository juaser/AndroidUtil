package com.plugin.weight.edittext;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.plugin.weight.R;


/**
 * 带搜索得EditText
 * Created by zxl on 2016/3/25.
 */
public class EditTextWithSearch extends EditText {
    private Drawable ic_search;
    private Context context;
    private OnClickSearchListerner listerner;
    public static final int type_right = 0;
    public static final int type_left = 1;
    private int type = 1;

    public EditTextWithSearch(Context context) {
        this(context, null, 0);
    }

    public EditTextWithSearch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditTextWithSearch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    /***
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (ic_search != null && event.getAction() == MotionEvent.ACTION_UP) {
            boolean isclick;
            if (type == type_left) {
                isclick = event.getX() > getPaddingLeft() && event.getX() < getTotalPaddingLeft();
            } else {
                isclick = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
            }
            if (isclick && listerner != null) {
                if (TextUtils.isEmpty(this.getText().toString())) {
                    listerner.click("");
                } else {
                    listerner.click(this.getText().toString());
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void init() {
        ic_search = ContextCompat.getDrawable(context, R.mipmap.ic_search);
        ic_search.setBounds(0, 0, ic_search.getIntrinsicWidth(), ic_search.getIntrinsicHeight());
        if (type == type_left) {
            setCompoundDrawables(ic_search, null, null, null);
        } else {
            setCompoundDrawables(null, null, ic_search, null);
        }
    }

    public void setOnSearchListerner(OnClickSearchListerner listerner) {
        this.listerner = listerner;
    }

    public interface OnClickSearchListerner {
        void click(String str);
    }

    public void setType(int type) {
        this.type = type;
        init();
    }
}
