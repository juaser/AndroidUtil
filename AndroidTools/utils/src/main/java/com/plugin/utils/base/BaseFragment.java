package com.plugin.utils.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plugin.utils.log.LogUtils;

import rx.Subscription;

/**
 * @类说明： 基本的Activity
 * @author：zxl
 * @CreateTime 2016/7/29.
 */
public class BaseFragment extends Fragment {
    protected String TAG = getClass().getName() + "====";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void le(String logmsg) {
        LogUtils.e(TAG + logmsg);
    }

    public void li(String logmsg) {
        LogUtils.i(TAG + logmsg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
