package com.zxl.androidtools.base;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * @Description:
 * @Author: zxl
 * @Date: 31/8/16.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
