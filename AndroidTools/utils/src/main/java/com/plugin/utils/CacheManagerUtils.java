package com.plugin.utils;

import android.content.Context;
import android.os.Environment;

import com.plugin.utils.log.LogUtils;
import com.plugin.utils.manager.AppManager;

import java.io.File;
import java.math.BigDecimal;

/**
 * @description： 清除缓存管理类
 * @author：zxl
 * @CreateTime 2016/8/12.
 */
public class CacheManagerUtils {
    private static volatile CacheManagerUtils mInstance = null;

    private CacheManagerUtils() {
    }

    public static CacheManagerUtils getInstance() {
        CacheManagerUtils instance = mInstance;
        if (instance == null) {
            synchronized (CacheManagerUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new CacheManagerUtils();
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
     * 获取整个缓存文件大小
     *
     * @param context
     * @return
     * @throws Exception
     */
    public String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = 0;
        try {
            cacheSize = getFolderSize(context.getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheSize += getFolderSize(context.getExternalCacheDir());
            }
        } catch (Exception e) {
            LogUtils.e("Exception");
        }

        return cacheSize == 0 ? "" : getFormatSize(cacheSize);
    }

    /**
     * 清空整个缓存
     */
    public void clearAllCache() {
        deleteDir(getContext().getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(getContext().getExternalCacheDir());
        }
    }

    /**
     * 删除某个文件
     *
     * @param dir
     * @return
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        if (dir == null) {
            return true;
        } else {
            return dir.delete();
        }
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            LogUtils.e("Exception");
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

}
