package com.zxl.androidtools.ui.customviews;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.utils.toast.T;
import com.plugin.weight.layout.FlowLayout;
import com.plugin.weight.layout.FlowLayoutWeightView;
import com.plugin.weight.textview.PigeonTextView;
import com.zxl.androidtools.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @description：
 * @author：zxl
 * @CreateTime 2016/8/22.
 */
public class FlowLayoutActivty extends BaseAppCompatActivity {
    @Bind(R.id.layout_flow)
    FlowLayout layoutFlow;

    @Bind(R.id.flowweight)
    FlowLayoutWeightView flowWeightView;

    private int num = 20;
    private List<View> mViews;

    @Override
    public int getLayoutId() {
        return R.layout.activity_flow;
    }

    public void initView() {
        initTest1();
        initTest2();
    }

    public void initTest1() {
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

    public void initTest2() {
        mViews = new ArrayList<>();
        flowWeightView.setOnItemClickListerner(new FlowLayoutWeightView.OnItemClickListerner() {
            @Override
            public void click(int position) {
                T.showShort("position--" + position);
            }
        });
        for (int i = 0; i < 6; i++) {
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setText("i==" + i);
            textView.setCompoundDrawablePadding(10);
            Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            textView.setCompoundDrawables(null, drawable, null, null);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mViews.add(textView);
        }
        flowWeightView.addLayoutViews(mViews);
    }
}
