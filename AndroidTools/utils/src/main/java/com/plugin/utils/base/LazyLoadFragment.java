package com.plugin.utils.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * @Description: 懒加载继承类
 * @Author: zxl
 * @Date: 2016/11/24 14:36
 */

public abstract class LazyLoadFragment extends Fragment {
    protected boolean isInit = false;
    protected boolean isLoad = false;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(setContentView(), container, false);
        isInit = true;
        return view;
    }

    /**
     * Fragment 要显示的布局
     */
    protected abstract int setContentView();

    protected View getContentView() {
        return view;
    }

    /**
     * 视图是否可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 加载数据条件1、视图初始化  2、视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit)
            return;
        if (getUserVisibleHint()) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad)
                stopLoad();
        }
    }

    /**
     * 视图初始化且对用户可见时，可以加载数据
     */
    protected abstract void lazyLoad();

    /**
     * 视图对用户不可见且加载过数据时，需要停止加载数据
     */
    protected abstract void stopLoad();

    protected void showToast(String message) {
        if (!TextUtils.isEmpty(message))
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 找出对应控件
     */
    protected <T extends View> T findViewById(int id) {
        return (T) getContentView().findViewById(id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoad = false;
    }
}
