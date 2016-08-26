package com.zxl.androidtools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.plugin.utils.base.BaseActivity;

/**
 * @Description:
 * @Author: zxl
 * @Date: 26/8/16.
 */
public class TestActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextCompat.getDrawable(this,R.drawable.shape_retangle_white);
    }
}
