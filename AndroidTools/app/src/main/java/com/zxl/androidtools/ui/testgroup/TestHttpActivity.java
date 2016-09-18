package com.zxl.androidtools.ui.testgroup;

import android.widget.TextView;

import com.plugin.utils.log.LogUtils;
import com.zxl.androidtools.R;
import com.zxl.androidtools.base.BaseMvpActivity;
import com.zxl.androidtools.ui.httpmvp.TestHttpContract;
import com.zxl.androidtools.ui.httpmvp.TestHttpModel;
import com.zxl.androidtools.ui.httpmvp.TestHttpPresenter;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 31/8/16.
 */
public class TestHttpActivity extends BaseMvpActivity<TestHttpPresenter, TestHttpModel> implements TestHttpContract.View

{
    @Bind(R.id.tv_testhttp)
    TextView tvTesthttp;

    @Override
    public int getLayoutId() {
        return R.layout.activity_testhttp;
    }

    @Override
    public void initView() {

    }

    @Override
    public void failed() {
        LogUtils.e("失败");
        tvTesthttp.setText("失败");
    }

    @Override
    public void success() {
        LogUtils.e("成功");
        tvTesthttp.setText("成功");
    }

    @OnClick(R.id.tv_testhttp)
    void click() {
        mPresenter.test("");
    }

    @OnClick(R.id.tv_testget)
    void httpGet() {
        mPresenter.test("get");
    }

    @OnClick(R.id.tv_testpost)
    void httpPost() {
        mPresenter.test("post");
    }

}
