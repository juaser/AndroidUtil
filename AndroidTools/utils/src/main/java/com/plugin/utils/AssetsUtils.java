package com.plugin.utils;

import android.content.Context;

import com.plugin.utils.log.LogUtils;
import com.plugin.utils.manager.AppManager;

import java.io.IOException;
import java.io.InputStream;

import static com.plugin.utils.IOUtils.close;

/**
 * @description： 操作安装包中的“assets”目录下的文件
 * @author：zxl
 * @CreateTime 2016/8/22.
 */
public class AssetsUtils {
    private static volatile AssetsUtils mInstance = null;

    private AssetsUtils() {
    }

    public static AssetsUtils getInstance() {
        AssetsUtils instance = mInstance;
        if (instance == null) {
            synchronized (AssetsUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new AssetsUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public Context getContext() {
        return AppManager.getInstance().getTop();
    }

    /**
     * read file content
     *
     * @param assetPath the asset path
     * @return String string
     */
    public String readFile2String(String assetPath) {
        InputStream is = null;
        try {
            is = getContext().getAssets().open(assetPath);
            return StringUtils.getInstance().input2String(is);
        } catch (IOException e) {
            LogUtils.e("IOException");
        } finally {
            close(is);
        }
        return "";
    }

    /**
     * read file content
     *
     * @param assetPath the asset path
     * @return String string
     */
    public InputStream readFile2InStream(String assetPath) {
        try {
            return getContext().getAssets().open(assetPath);
        } catch (IOException e) {
            LogUtils.e("IOException");
        }
        return null;
    }
}
