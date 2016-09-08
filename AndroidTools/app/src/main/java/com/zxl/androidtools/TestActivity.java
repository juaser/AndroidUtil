package com.zxl.androidtools;

import android.widget.TextView;

import com.plugin.utils.base.BaseAppCompatActivity;

import butterknife.Bind;

/**
 * @Description:
 * @Author: zxl
 * @Date: 26/8/16.
 */
public class TestActivity extends BaseAppCompatActivity {
    @Bind(R.id.tv_test)
    TextView tvTest;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView() {

    }
}
