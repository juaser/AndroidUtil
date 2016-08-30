package com.plugin.utils;

import com.plugin.utils.log.LogUtils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @Description:
 * @Author: zxl
 * @Date: 30/8/16.
 */
public class IOUtils {
    /**
     * 关闭流
     */
    public static boolean close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                LogUtils.e("IOException");
            }
        }
        return true;
    }
}
