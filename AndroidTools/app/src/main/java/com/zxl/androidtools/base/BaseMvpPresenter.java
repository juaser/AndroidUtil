package com.zxl.androidtools.base;

/**
 * @Description:
 * @Author: zxl
 * @Date: 31/8/16.
 */
public class BaseMvpPresenter<M, V> {
    public M mModel;
    public V mView;

    public void setMV(M m, V v) {
        this.mModel = m;
        this.mView = v;
    }
}
