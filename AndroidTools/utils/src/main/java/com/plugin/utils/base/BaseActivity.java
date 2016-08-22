package com.plugin.utils.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.plugin.utils.log.LogUtils;
import com.plugin.utils.manager.AppManager;

/**
 * @类说明：
 * @author：zxl
 * @CreateTime 2016/7/27.
 */
public abstract class BaseActivity extends FragmentActivity {
    protected String TAG = getClass().getName() + "====";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().add(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().remove(this);
    }

    public void le(String logmsg) {
        LogUtils.e(TAG + logmsg);
    }

    public void li(String logmsg) {
        LogUtils.i(TAG + logmsg);
    }

}
