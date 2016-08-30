package com.zxl.androidtools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.plugin.utils.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @Description:
 * @Author: zxl
 * @Date: 26/8/16.
 */
public class TestActivity extends BaseActivity {
    @Bind(R.id.tv_test)
    TextView tvTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
