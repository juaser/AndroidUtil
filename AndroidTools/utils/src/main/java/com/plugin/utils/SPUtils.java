package com.plugin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.plugin.utils.manager.AppManager;


/**
 * @Description: SP读写工具类
 * @Author: zxl
 * @Date: 1/9/16 上午11:12.
 */
public class SPUtils {

    private static volatile SPUtils mInstance = null;

    private SPUtils() {
    }

    public static SPUtils getInstance() {
        SPUtils instance = mInstance;
        if (instance == null) {
            synchronized (SPUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new SPUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /**
     * @description: 获取上下文
     */
    public Context getContext() {
        return AppManager.getInstance().getTop();
    }

    /**
     * SP的name值
     * <p>可通过修改PREFERENCE_NAME变量修改SP的name值</p>
     */
    public static String PREFERENCE_NAME = "ANCROID_UTIL_CODE";

    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     * @return true: 写入成功<br>false: 写入失败
     */
    public  boolean putString(String key, String value) {
        return getSP().edit().putString(key, value).commit();
    }

    /**
     * SP中读取String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值null
     */
    public  String getString(String key) {
        return getString( key, null);
    }

    /**
     * SP中读取String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值defaultValue
     */
    public  String getString(String key, String defaultValue) {
        return getSP().getString(key, defaultValue);
    }

    /**
     * SP中写入int类型value
     *
     * @param key   键
     * @param value 值
     * @return true: 写入成功<br>false: 写入失败
     */
    public  boolean putInt(String key, int value) {
        return getSP().edit().putInt(key, value).commit();
    }

    /**
     * SP中读取int
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public  int getInt(String key) {
        return getInt(key, -1);
    }

    /**
     * SP中读取int
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值defaultValue
     */
    public  int getInt(String key, int defaultValue) {
        return getSP().getInt(key, defaultValue);
    }

    /**
     * SP中写入long类型value
     *
     * @param key   键
     * @param value 值
     * @return true: 写入成功<br>false: 写入失败
     */
    public  boolean putLong(String key, long value) {
        return getSP().edit().putLong(key, value).commit();
    }

    /**
     * SP中读取long
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public  long getLong(String key) {
        return getLong(key, -1);
    }

    /**
     * SP中读取long
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值defaultValue
     */
    public  long getLong(String key, long defaultValue) {
        return getSP().getLong(key, defaultValue);
    }

    /**
     * SP中写入float类型value
     *
     * @param key   键
     * @param value 值
     * @return true: 写入成功<br>false: 写入失败
     */
    public  boolean putFloat(String key, float value) {
        return getSP().edit().putFloat(key, value).commit();
    }

    /**
     * SP中读取float
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public  float getFloat(String key) {
        return getFloat( key, -1);
    }

    /**
     * SP中读取float
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值defaultValue
     */
    public  float getFloat(String key, float defaultValue) {
        return getSP().getFloat(key, defaultValue);
    }

    /**
     * SP中写入boolean类型value
     *
     * @param key   键
     * @param value 值
     * @return true: 写入成功<br>false: 写入失败
     */
    public  boolean putBoolean(String key, boolean value) {
        return getSP().edit().putBoolean(key, value).commit();
    }

    /**
     * SP中读取boolean
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值false
     */
    public  boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * SP中读取boolean
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值defaultValue
     */
    public  boolean getBoolean(String key, boolean defaultValue) {
        return getSP().getBoolean(key, defaultValue);
    }

    /**
     * 获取name为PREFERENCE_NAME的SP对象
     *
     * @return SP
     */
    private SharedPreferences getSP() {
        return getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }
}
