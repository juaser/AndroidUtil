package com.zxl.androidtools.ui.animations;

import android.widget.TextView;

import com.plugin.utils.JitterUtils;
import com.plugin.utils.base.BaseAppCompatActivity;
import com.zxl.androidtools.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description: 抖动效果
 * @Author: zxl
 * @Date: 5/9/16 下午6:02.
 */
public class JitterViewActivity extends BaseAppCompatActivity {
    @Bind(R.id.tv_jitter1)
    TextView tvJitter1;
    @Bind(R.id.tv_jitter1_btn1)
    TextView tvJitter1Btn;
    @Bind(R.id.tv_jitter2)
    TextView tvJitter2;
    @Bind(R.id.tv_jitter1_btn2)
    TextView tvJitter1Btn2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_jitterview;
    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.tv_jitter1_btn1)
    void btn1() {
        JitterUtils.getInstance().tada(tvJitter1);
    }

    @OnClick(R.id.tv_jitter1_btn2)
    void btn2() {
        JitterUtils.getInstance().nope(tvJitter2);
    }

}
