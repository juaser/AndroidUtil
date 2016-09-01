package com.plugin.utils;

import com.plugin.utils.log.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.plugin.utils.IOUtils.close;

/**
 * @Description: 文件操作工具类
 * @Author: zxl
 * @Date: 1/9/16 上午10:38.
 */
public class FileUtils {
    private static volatile FileUtils mInstance = null;

    private FileUtils() {
    }

    public static FileUtils getInstance() {
        FileUtils instance = mInstance;
        if (instance == null) {
            synchronized (FileUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new FileUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /**
     * 复制文件，可以选择是否删除源文件
     */
    public boolean copyFile(String srcPath, String destPath,
                            boolean deleteSrc) {
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);
        return copyFile(srcFile, destFile, deleteSrc);
    }

    /**
     * 复制文件，可以选择是否删除源文件
     */
    public boolean copyFile(File srcFile, File destFile,
                            boolean deleteSrc) {
        if (!srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = in.read(buffer)) > 0) {
                out.write(buffer, 0, i);
                out.flush();
            }
            if (deleteSrc) {
                srcFile.delete();
            }
        } catch (FileNotFoundException e) {
            LogUtils.e("FileNotFoundException");
            return false;
        } catch (IOException e) {
            LogUtils.e("IOException");
            return false;
        } finally {
            close(out);
            close(in);
        }
        return true;
    }

    /**
     * 判断文件是否可写
     */
    public boolean isWriteable(String path) {
        File f = new File(path);
        if (f.exists() && f.isFile() && f.canRead()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 修改文件的权限,例如"777"等
     */
    public void chmod(String path, String mode) {
        String command = "chmod " + mode + " " + path;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            LogUtils.e("IOException");
        }
    }

    /**
     * 把数据写入文件
     *
     * @param is       数据流
     * @param path     文件路径
     * @param recreate 如果文件存在，是否需要删除重建
     * @return 是否写入成功
     */
    public boolean writeFile(InputStream is, String path,
                             boolean recreate) {
        boolean res = false;
        File f = new File(path);
        FileOutputStream fos = null;
        try {
            if (recreate && f.exists()) {
                f.delete();
            }
            if (!f.exists() && null != is) {
                File parentFile = new File(f.getParent());
                parentFile.mkdirs();
                int count = -1;
                byte[] buffer = new byte[1024];
                fos = new FileOutputStream(f);
                while ((count = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                }
                res = true;
            }
        } catch (FileNotFoundException e) {
            LogUtils.e("FileNotFoundException");
        } catch (IOException e) {
            LogUtils.e("IOException");
        } finally {
            close(fos);
            close(is);
        }
        return res;
    }

    /**
     * 把字符串数据写入文件
     *
     * @param content 需要写入的字符串
     * @param path    文件路径名称
     * @param append  是否以添加的模式写入
     * @return 是否写入成功
     */
    public boolean writeFile(byte[] content, String path, boolean append) {
        boolean res = false;
        File f = new File(path);
        RandomAccessFile raf = null;
        try {
            if (f.exists()) {
                if (!append) {
                    f.delete();
                    f.createNewFile();
                }
            } else {
                f.createNewFile();
            }
            if (f.canWrite()) {
                raf = new RandomAccessFile(f, "rw");
                raf.seek(raf.length());
                raf.write(content);
                res = true;
            }
        } catch (FileNotFoundException e) {
            LogUtils.e("FileNotFoundException");
        } catch (IOException e) {
            LogUtils.e("IOException");
        } finally {
            close(raf);
        }
        return res;
    }

    /**
     * 把字符串数据写入文件
     *
     * @param content 需要写入的字符串
     * @param path    文件路径名称
     * @param append  是否以添加的模式写入
     * @return 是否写入成功
     */
    public boolean writeFile(String content, String path, boolean append) {
        return writeFile(content.getBytes(), path, append);
    }

    /**
     * 把键值对写入文件
     *
     * @param filePath 文件路径
     * @param key      键
     * @param value    值
     * @param comment  该键值对的注释
     */
    public void writeProperties(String filePath, String key,
                                String value, String comment) {
        if (StringUtils.getInstance().isEmpty(key) || StringUtils.getInstance().isEmpty(filePath)) {
            return;
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        File f = new File(filePath);
        try {
            if (!f.exists() || !f.isFile()) {
                f.createNewFile();
            }
            fis = new FileInputStream(f);
            Properties p = new Properties();
            p.load(fis);// 先读取文件，再把键值对追加到后面
            p.setProperty(key, value);
            fos = new FileOutputStream(f);
            p.store(fos, comment);
        } catch (FileNotFoundException e) {
            LogUtils.e("FileNotFoundException");
        } catch (IOException e) {
            LogUtils.e("IOException");
        } finally {
            close(fis);
            close(fos);
        }
    }

    /**
     * 根据值读取
     */
    public String readProperties(String filePath, String key,
                                 String defaultValue) {
        if (StringUtils.getInstance().isEmpty(key) || StringUtils.getInstance().isEmpty(filePath)) {
            return null;
        }
        String value = null;
        FileInputStream fis = null;
        File f = new File(filePath);
        try {
            if (!f.exists() || !f.isFile()) {
                f.createNewFile();
            }
            fis = new FileInputStream(f);
            Properties p = new Properties();
            p.load(fis);
            value = p.getProperty(key, defaultValue);
        } catch (IOException e) {
            LogUtils.e(e);
        } finally {
            close(fis);
        }
        return value;
    }

    /**
     * 把字符串键值对的map写入文件
     */
    public void writeMap(String filePath, Map<String, String> map,
                         boolean append, String comment) {
        if (map == null || map.size() == 0 || StringUtils.getInstance().isEmpty(filePath)) {
            return;
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        File f = new File(filePath);
        try {
            if (!f.exists() || !f.isFile()) {
                f.createNewFile();
            }
            Properties p = new Properties();
            if (append) {
                fis = new FileInputStream(f);
                p.load(fis);// 先读取文件，再把键值对追加到后面
            }
            p.putAll(map);
            fos = new FileOutputStream(f);
            p.store(fos, comment);
        } catch (FileNotFoundException e) {
            LogUtils.e("FileNotFoundException");
        } catch (IOException e) {
            LogUtils.e("IOException");
        } finally {
            close(fis);
            close(fos);
        }
    }

    /**
     * 把字符串键值对的文件读入map
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map<String, String> readMap(String filePath,
                                       String defaultValue) {
        if (StringUtils.getInstance().isEmpty(filePath)) {
            return null;
        }
        Map<String, String> map = null;
        FileInputStream fis = null;
        File f = new File(filePath);
        try {
            if (!f.exists() || !f.isFile()) {
                f.createNewFile();
            }
            fis = new FileInputStream(f);
            Properties p = new Properties();
            p.load(fis);
            map = new HashMap<String, String>((Map) p);// 因为properties继承了map，所以直接通过p来构造一个map
        } catch (FileNotFoundException e) {
            LogUtils.e("FileNotFoundException");
        } catch (IOException e) {
            LogUtils.e("IOException");
        } finally {
            close(fis);
        }
        return map;
    }

    /**
     * 改名
     */
    public boolean copy(String src, String des, boolean delete) {
        File file = new File(src);
        if (!file.exists()) {
            return false;
        }
        File desFile = new File(des);
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(file);
            out = new FileOutputStream(desFile);
            byte[] buffer = new byte[1024];
            int count = -1;
            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
                out.flush();
            }
        } catch (FileNotFoundException e) {
            LogUtils.e("FileNotFoundException");
            return false;
        } catch (IOException e) {
            LogUtils.e("IOException");
            return false;
        } finally {
            close(in);
            close(out);
        }
        if (delete) {
            file.delete();
        }
        return true;
    }
}
