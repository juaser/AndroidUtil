package com.zxl.androidtools;

import android.widget.TextView;

import com.plugin.utils.AppUtils;
import com.plugin.utils.base.BaseAppCompatActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 26/8/16.
 */
public class TestActivity extends BaseAppCompatActivity {
    @Bind(R.id.tv_test)
    TextView tvTest;

    float scale = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView() {
    }

    @OnClick(R.id.tv_test)
    void click() {
        if (scale == 1) {
            scale = 1.1f;
        } else {
            scale = 1;
        }
        tvTest.animate().scaleX(scale);
        tvTest.animate().scaleY(scale);
        AppUtils.getInstance().shareAppInfo("测试");
    }
}
