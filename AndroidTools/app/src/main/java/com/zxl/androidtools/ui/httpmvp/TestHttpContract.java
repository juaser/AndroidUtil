package com.zxl.androidtools.ui.httpmvp;

import com.zxl.androidtools.base.BaseMvpModel;
import com.zxl.androidtools.base.BaseMvpPresenter;
import com.zxl.androidtools.base.BaseMvpView;

/**
 * @Description:
 * @Author: zxl
 * @Date: 31/8/16.
 */
public interface TestHttpContract {
    interface View extends BaseMvpView {
        void failed();

        void success();
    }

    interface Model extends BaseMvpModel {
        String test(String testStr);
    }

    abstract class Presenter extends BaseMvpPresenter<Model, View> {
        public abstract void test(String testStr);
    }
}
