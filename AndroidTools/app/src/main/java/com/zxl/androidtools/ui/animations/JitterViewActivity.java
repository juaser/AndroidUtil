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
    @Bind(R.id.tv_jitter3)
    TextView tvJitter3;
    @Bind(R.id.tv_jitter1_btn3)
    TextView tvJitter1Btn3;
    @Bind(R.id.tv_jitter4)
    TextView tvJitter4;
    @Bind(R.id.tv_jitter1_btn4)
    TextView tvJitter1Btn4;

    @Override
    public int getLayoutId() {
        return R.layout.activity_jitterview;
    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.tv_jitter1_btn1)
    void btn1() {
        JitterUtils.getInstance().jitter_Y(tvJitter1);
    }

    @OnClick(R.id.tv_jitter1_btn2)
    void btn2() {
        JitterUtils.getInstance().jitter_X(tvJitter2);
    }

    @OnClick(R.id.tv_jitter1_btn3)
    void btn3() {
        JitterUtils.getInstance().jitter1(tvJitter3);
    }

    @OnClick(R.id.tv_jitter1_btn4)
    void btn4() {
        JitterUtils.getInstance().jitter2(tvJitter4);
    }

}
