package com.zxl.androidtools;

import android.widget.TextView;

import com.plugin.utils.PathUtils;
import com.plugin.utils.base.BaseActivity;

import butterknife.Bind;

/**
 * @Description:
 * @Author: zxl
 * @Date: 26/8/16.
 */
public class TestActivity extends BaseActivity {
    @Bind(R.id.tv_test)
    TextView tvTest;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView() {
        String imagepath = PathUtils.getInstance().getDemoDir();
    }
}
