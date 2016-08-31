package com.zxl.androidtools.ui.httpmvp;

import android.text.TextUtils;

/**
 * @Description:
 * @Author: zxl
 * @Date: 31/8/16.
 */
public class TestHttpPresenter extends TestHttpContract.Presenter {
    @Override
    public void test(String testStr) {
        if (TextUtils.isEmpty(testStr)) {
            mView.failed();
        } else {
            mModel.test("test" + testStr);
        }
    }
}
