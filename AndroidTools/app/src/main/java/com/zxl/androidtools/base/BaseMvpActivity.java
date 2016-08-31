package com.zxl.androidtools.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.plugin.utils.ReflectUtils;
import com.plugin.utils.base.BaseActivity;

/**
 * @Description:
 * @Author: zxl
 * @Date: 31/8/16.
 */
public abstract class BaseMvpActivity<P extends BaseMvpPresenter, M extends BaseMvpModel> extends BaseActivity {

    public P mPresenter;
    public M mModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initPresenter();
        super.onCreate(savedInstanceState);
    }

    /**
     * 通过反射来生成实例
     */
    public void initPresenter() {
        mPresenter = ReflectUtils.getT(this, 0);
        mModel = ReflectUtils.getT(this, 1);
        if (this instanceof BaseMvpView)
            mPresenter.setMV(mModel, this);
    }
}
