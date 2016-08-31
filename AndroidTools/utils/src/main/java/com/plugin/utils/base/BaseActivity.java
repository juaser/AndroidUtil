package com.plugin.utils.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.plugin.utils.log.LogUtils;
import com.plugin.utils.manager.AppManager;

import butterknife.ButterKnife;

/**
 * @类说明：
 * @author：zxl
 * @CreateTime 2016/7/27.
 */
public abstract class BaseActivity extends FragmentActivity {
    protected String TAG = getClass().getName() + "====";
    public Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        AppManager.getInstance().add(this);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        AppManager.getInstance().remove(this);
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public void le(String logmsg) {
        LogUtils.e(TAG + logmsg);
    }

    public void ld(String logmsg) {
        LogUtils.d(TAG + logmsg);
    }

}
