package com.plugin.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.plugin.utils.log.LogUtils;
import com.plugin.utils.manager.AppManager;

import java.io.File;
import java.io.IOException;

/**
 * @Description:
 * @Author: zxl
 * @Date: 31/8/16.
 */
public class PathUtils {
    private static volatile PathUtils mInstance = null;
    private static final String dir_download = "new_download";
    private static final String dir_image = "new_image";
    private static final String dir_voice = "new_voice";
    private static final String dir_video = "new_video";
    private static final String dir_cache = "new_cache";
    private static final String dir_demo = "zxl_demo";
    private String root_path = null;
    private String current_path = null;

    private PathUtils() {
    }

    public static PathUtils getInstance() {
        PathUtils instance = mInstance;
        if (instance == null) {
            synchronized (PathUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new PathUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /**
     * 获取当前的上下文
     *
     * @return
     */
    public Context getContext() {
        return AppManager.getInstance().getTop();
    }

    /**
     * 判断SD卡是否挂载
     */
    public static boolean isSDCardAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取手机系统根目录
     * 这里说的系统仅仅指"/system"
     * 不包括外部存储的手机存储的范围远远大于所谓的系统存储
     *
     * @return
     */
    public String getPhoneRootDir() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * 获取手机SD卡目录
     *
     * @return
     */
    public String getPhoneExternalDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取data根目录
     *
     * @return
     */
    public String getPhoneDataDir() {
        return Environment.getDataDirectory().getAbsolutePath();
    }

    /**
     * 此处返回的路劲为/data/data/包/files，其中的包就是我们建立的主Activity所在的包
     *
     * @return
     */
    public String getPhonePrivateDir() {
        return getContext().getFilesDir().getAbsolutePath();
    }

    /**
     * 判断SD卡是否挂载，确定要放置文件的根目录
     */
    public String getRootDir() {
        root_path = null;
        if (isSDCardAvailable()) {
            root_path = getPhoneExternalDir();
        } else {
            root_path = getPhoneRootDir();
        }
        return root_path;
    }

    /**
     * 获取存储图片的文件夹
     *
     * @return
     */
    public String getImageDir() {
        current_path = getRootDir() + File.separator + dir_image;
        isDirExits(current_path, true);
        return current_path;
    }

    /**
     * 获取下载的文件夹
     *
     * @return
     */
    public String getDownloadDir() {
        current_path = getRootDir() + File.separator + dir_download;
        isDirExits(current_path, true);
        return current_path;
    }

    /**
     * 自定义文件夹
     *
     * @return
     */
    public String getDemoDir() {
        current_path = getRootDir() + File.separator + dir_demo;
        isDirExits(current_path, true);
        return current_path;
    }

    /**
     * 判断文件夹是否存在
     * mkdir()是假设该路径的父目录是已经存在的，即只创建最后一层目录；
     * mkdirs()则不必考虑父目录是否存在，假如不存在，会一层层地创建目录
     *
     * @param path     文件夹的路径
     * @param isCreate 如果不存在是否创建
     * @return
     */
    public boolean isDirExits(String path, boolean isCreate) {
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            if (isCreate) {
                LogUtils.e("创建文件夹==" + path);
                file.mkdirs();
            }
            return !(!file.exists() && !file.isDirectory());
        } else {
            LogUtils.e("文件夹已存在==" + path);
            return true;
        }
    }

    /**
     * 遍历整个文件夹
     *
     * @param path
     */
    public void traverseDir(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            StringBuffer buffer = new StringBuffer();
            buffer.append("要遍历的文件夹下的文件：" + path + "\n");
            for (File f : files) {
                buffer.append(f.getName() + "\n");
            }
            LogUtils.e(buffer.toString());
        } else {
            LogUtils.e("不是个文件夹");
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param path     文件的路径
     * @param isCreate 如果不存在是否创建
     * @return
     */

    public boolean isFileExits(String path, boolean isCreate) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LogUtils.e("创建文件失败==" + path);
            }
            return file.exists();
        } else {
            return true;
        }
    }

    /**
     * 查看SD卡的剩余空间
     *
     * @return
     */
    public long getSDFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
    }

    /**
     * 查看SD卡总容量
     *
     * @return
     */
    public long getSDAllSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡大小
        //return allBlocks * blockSize; //单位Byte
        //return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize) / 1024 / 1024; //单位MB
    }
}
