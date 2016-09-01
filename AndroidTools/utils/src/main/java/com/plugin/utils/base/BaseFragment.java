package com.plugin.utils.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plugin.utils.log.LogUtils;

import butterknife.ButterKnife;

/**
 * @类说明： 基本的Fragment
 * @author：zxl
 * @CreateTime 2016/7/29.
 */
public abstract class BaseFragment extends Fragment {
    protected String TAG = getClass().getName() + "====";
    public Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = LayoutInflater.from(getActivity()).inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public void le(String logmsg) {
        LogUtils.e(TAG + logmsg);
    }

    public void ld(String logmsg) {
        LogUtils.d(TAG + logmsg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
