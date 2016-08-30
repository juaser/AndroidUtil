package com.plugin.utils;

import android.content.Context;

import com.plugin.utils.log.LogUtils;

import java.io.IOException;
import java.io.InputStream;


/**
 * 操作安装包中的“assets”目录下的文件
 */
public class AssetsUtils {

    /**
     * read file content
     *
     * @param context   the context
     * @param assetPath the asset path
     * @return String string
     */
    public static String readFile2String(Context context, String assetPath) {
        InputStream is = null;
        try {
            is = context.getAssets().open(assetPath);
            return StringUtils.toString(is);
        } catch (IOException e) {
            LogUtils.e("IOException");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LogUtils.e("IOException");
                }
            }
        }
        return "";
    }

    /**
     * read file content
     *
     * @param context   the context
     * @param assetPath the asset path
     * @return String string
     */
    public static InputStream readFile2InStream(Context context, String assetPath) {
        try {
            return context.getAssets().open(assetPath);
        } catch (IOException e) {
            LogUtils.e("IOException");
        }
        return null;
    }
}
