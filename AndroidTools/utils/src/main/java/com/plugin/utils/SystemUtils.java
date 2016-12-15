package com.plugin.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.plugin.utils.log.LogUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;

/**
 * Android的系统方法
 * Created by zxl on 2016/11/4.
 */
public class SystemUtils {
    private static volatile SystemUtils mInstance = null;

    private SystemUtils() {
    }

    public static SystemUtils getInstance() {
        SystemUtils instance = mInstance;
        if (instance == null) {
            synchronized (SystemUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new SystemUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }












    //TODO 判断当前是否可用
    public boolean isNetAvailable(Context context) {
        //<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>  <uses-permission android:name="android.permission.INTERNET"/>
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return true;
            }
        }
        return false;
    }
}
