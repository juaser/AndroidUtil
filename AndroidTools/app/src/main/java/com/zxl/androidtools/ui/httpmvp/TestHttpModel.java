package com.zxl.androidtools.ui.httpmvp;

import com.plugin.utils.toast.T;

/**
 * @Description:
 * @Author: zxl
 * @Date: 31/8/16.
 */
public class TestHttpModel implements TestHttpContract.Model {
    @Override
    public String test(String testStr) {
        T.showShort(testStr);
        return testStr;
    }
}
