package com.zxl.androidtools.ui.customviews;

import android.view.View;
import android.view.ViewGroup;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.utils.toast.T;
import com.plugin.weight.layout.FlowLayout;
import com.plugin.weight.textview.PigeonTextView;
import com.zxl.androidtools.R;

import butterknife.Bind;

/**
 * @description：
 * @author：zxl
 * @CreateTime 2016/8/22.
 */
public class FlowLayoutActivty extends BaseAppCompatActivity {
    @Bind(R.id.layout_flow)
    FlowLayout layoutFlow;

    private int num = 20;

    @Override
    public int getLayoutId() {
        return R.layout.activity_flow;
    }

    public void initView() {
        for (int i = 0; i < num; i++) {
            PigeonTextView pigeonTextView = new PigeonTextView(this);
            pigeonTextView.setText("选项" + i);
            layoutFlow.addView(pigeonTextView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        layoutFlow.setOnItemClickListerner(new FlowLayout.onItemClickListerner() {
            @Override
            public void click(int position, View view) {
                T.showShort("position--" + position);
            }
        });
    }
}
