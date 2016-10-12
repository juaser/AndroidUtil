package com.plugin.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * @Description: SP读写工具类
 * @Author: zxl
 * @Date: 1/9/16 上午11:12.
 */
public class SPUtils {

    private static volatile SPUtils mInstance = null;

    /**
     * SP的name值
     */
    public static String PREFERENCE_NAME = "ANCROID_UTIL_CODE";

    private static SharedPreferences sharedPreferences = null;

    private SPUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static SPUtils getInstance(Context context) {
        SPUtils instance = mInstance;
        if (instance == null) {
            synchronized (SPUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new SPUtils(context);
                    mInstance = instance;
                }
            }
        }
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return instance;
    }

    /**
     * SP中写入String类型value
     */
    public boolean putString(String key, String value) {
        return sharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * SP中读取String 不存在返回默认值null
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * SP中读取String  不存在返回默认值defaultValue
     */
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * SP中写入int类型value
     */
    public boolean putInt(String key, int value) {
        return sharedPreferences.edit().putInt(key, value).commit();
    }

    /**
     * SP中读取int 不存在返回默认值-1
     */
    public int getInt(String key) {
        return getInt(key, -1);
    }

    /**
     * SP中读取int 不存在返回默认值defaultValue
     */
    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**
     * SP中写入long类型value
     */
    public boolean putLong(String key, long value) {
        return sharedPreferences.edit().putLong(key, value).commit();
    }

    /**
     * SP中读取long 不存在返回默认值-1
     */
    public long getLong(String key) {
        return getLong(key, -1);
    }

    /**
     * SP中读取long 不存在返回默认值defaultValue
     */
    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    /**
     * SP中写入float类型value
     */
    public boolean putFloat(String key, float value) {
        return sharedPreferences.edit().putFloat(key, value).commit();
    }

    /**
     * SP中读取float 不存在返回默认值-1
     */
    public float getFloat(String key) {
        return getFloat(key, -1);
    }

    /**
     * SP中读取float  不存在返回默认值defaultValue
     */
    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    /**
     * SP中写入boolean类型value
     */
    public boolean putBoolean(String key, boolean value) {
        return sharedPreferences.edit().putBoolean(key, value).commit();
    }

    /**
     * SP中读取boolean 不存在返回默认值false
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * SP中读取boolean  不存在返回默认值defaultValue
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }
}
