package com.zxl.androidtools.ui;

import android.view.View;
import android.widget.TextView;

import com.plugin.utils.base.BaseActivity;
import com.plugin.utils.toast.T;
import com.plugin.weight.textview.UPMarqueeView;
import com.plugin.weight.textview.VerticalSwitchTextView;
import com.zxl.androidtools.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @description：
 * @author：zxl
 * @CreateTime 2016/8/23.
 */
public class MarqueeActivity extends BaseActivity {
    @Bind(R.id.upMarquee)
    UPMarqueeView upMarquee;
    List<View> views;
    @Bind(R.id.vertical_switch_textview1)
    VerticalSwitchTextView verticalSwitchTextview1;
    @Bind(R.id.vertical_switch_textview2)
    VerticalSwitchTextView verticalSwitchTextview2;
    List<String> marquees;

    @Override
    public int getLayoutId() {
        return R.layout.activity_marquee;
    }

    public void initView() {
        views = new ArrayList<>();
        TextView textView = new TextView(this);
        textView.setText("仿淘宝首页的淘宝头条垂直滚动");
        views.add(textView);
        marquees = new ArrayList<>();
        marquees.add("11111");
        marquees.add("22222");
        setView();
    }

    public void setView() {
        upMarquee.setViews(views);
        upMarquee.setOnItemClickListener(new UPMarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                T.showShort("position==" + position);
            }
        });
        verticalSwitchTextview1.setTextContent(marquees);
        verticalSwitchTextview2.setTextContent(marquees);

    }
}
