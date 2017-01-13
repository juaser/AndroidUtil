package com.zxl.androidtools.ui.systemviews;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.utils.log.LogUtils;
import com.zxl.androidtools.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description: 软键盘遮挡Edit的问题
 * @Author: zxl
 * @Date: 2017/1/10 10:08
 */

public class EditCoveredActivity extends BaseAppCompatActivity {
    @Bind(R.id.tv_type1)
    TextView tvType1;
    @Bind(R.id.tv_type2)
    TextView tvType2;
    @Bind(R.id.layout_content)
    LinearLayout layoutContent;
    @Bind(R.id.layout_scrollview)
    ScrollView layoutScrollview;
    int style = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_editcovered;
    }

    @Override
    public void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        layoutScrollview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                layoutScrollview.getWindowVisibleDisplayFrame(rect);
                int rootInvisibleHeight = layoutScrollview.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    if (style == 1) {
                        final int scroll_height = layoutScrollview.getHeight();
                        final int content_height = layoutContent.getHeight();
                        LogUtils.e("scroll_height==" + scroll_height + "    content_height==" + content_height);
                        if (content_height >= scroll_height) {
                            layoutScrollview.post(new Runnable() {
                                @Override
                                public void run() {
                                    layoutScrollview.scrollTo(0, content_height - scroll_height);
                                }
                            });
                        }
                    } else {
                        int[] location = new int[2];
                        View focus = getCurrentFocus();
                        if (focus != null) {
                            focus.getLocationInWindow(location);
                            int scrollHeight = (location[1] + focus.getHeight()) - rect.bottom;
                            if (rect.bottom < location[1] + focus.getHeight()) {
                                layoutScrollview.scrollTo(0, scrollHeight);
                            }
                        }
                    }
                } else {
                    //键盘隐藏
                    layoutScrollview.scrollTo(0, 0);
                }
            }
        });
    }

    @OnClick(R.id.tv_type1)
    void click1() {
        style = 1;
        tvType1.setBackgroundResource(R.drawable.shape_edit_white_gray);
        tvType2.setBackgroundResource(R.drawable.shape_retangle_white);
    }

    @OnClick(R.id.tv_type2)
    void click2() {
        style = 2;
        tvType1.setBackgroundResource(R.drawable.shape_retangle_white);
        tvType2.setBackgroundResource(R.drawable.shape_edit_white_gray);
    }
}
