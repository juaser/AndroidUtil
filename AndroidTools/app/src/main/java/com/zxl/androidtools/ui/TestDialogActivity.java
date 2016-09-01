package com.zxl.androidtools.ui;

import com.plugin.utils.DialogUtils;
import com.plugin.utils.base.BaseActivity;
import com.zxl.androidtools.R;

import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 1/9/16 下午12:29.
 */
public class TestDialogActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_testdialog;
    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.tv_loadingdialog)
    public void show() {
        DialogUtils.getInstance().showDialog(R.layout.layout_loading);
    }
}
