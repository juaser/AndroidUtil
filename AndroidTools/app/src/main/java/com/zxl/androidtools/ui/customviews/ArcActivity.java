package com.zxl.androidtools.ui.customviews;

import android.widget.TextView;

import com.plugin.utils.base.BaseActivity;
import com.plugin.weight.arc.ArcRing4View;
import com.zxl.androidtools.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description
 * @Created by zxl on 8/10/16.
 */

public class ArcActivity extends BaseActivity {
    @Bind(R.id.tv_start)
    TextView tvStart;
    @Bind(R.id.arc_4)
    ArcRing4View arc4;

    @Override
    public int getLayoutId() {
        return R.layout.activity_arc;
    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.tv_start)
    void start() {
        arc4.start();
    }
}
