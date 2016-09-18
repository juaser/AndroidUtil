package com.zxl.androidtools.ui.testgroup;

import android.widget.TextView;

import com.plugin.utils.base.BaseAppCompatActivity;
import com.zxl.androidtools.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 12/9/16 PM12:23.
 */
public class TestShadowActivity extends BaseAppCompatActivity {
    @Bind(R.id.tv_shadow)
    TextView tvShadow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shadow;
    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.tv_shadow)
    void shadow() {

    }
}
