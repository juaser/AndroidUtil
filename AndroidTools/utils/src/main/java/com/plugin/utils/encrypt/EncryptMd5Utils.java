package com.plugin.utils.encrypt;

import com.plugin.utils.log.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.plugin.utils.IOUtils.close;

/**
 * @Description: MD5加密 MD5 加密不可逆 主要就是校验
 * @Author: zxl
 * @Date: 1/9/16 上午10:34.
 */
public class EncryptMd5Utils {
    private static volatile EncryptMd5Utils mInstance = null;
    private static final String key = "EncryptMd5Utils";
    private static final String TYPE_MD5 = "MD5";
    private static final String TYPE_SHA_256 = "SHA-256";
    private static final String TYPE_SHA_512 = "SHA-512";

    private EncryptMd5Utils() {
    }

    public static EncryptMd5Utils getInstance() {
        EncryptMd5Utils instance = mInstance;
        if (instance == null) {
            synchronized (EncryptMd5Utils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new EncryptMd5Utils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /**
     * 获取MD5校验码
     *
     * @param data 校验数据
     * @return
     */
    public String encryptMD5(byte[] data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(TYPE_MD5);
            byte[] keyByte = key.getBytes();
            byte[] resultByte = new byte[data.length + keyByte.length];
            System.arraycopy(keyByte, 0, resultByte, 0, keyByte.length);
            System.arraycopy(data, 0, resultByte, keyByte.length, data.length);
            md.update(resultByte);
            return bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e("NoSuchAlgorithmException");
            return "";
        }
    }

    /**
     * 获取文件的MD5校验码
     *
     * @param file
     * @return
     */
    public String encriptFile2MD5(File file) {
        FileInputStream in = null;
        FileChannel channel = null;
        try {
            in = new FileInputStream(file);
            channel = in.getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buffer);
            return bytes2Hex(md.digest());
        } catch (FileNotFoundException e) {
            LogUtils.e("FileNotFoundException");
            return "";
        } catch (IOException e) {
            LogUtils.e("IOException");
            return "";
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e("NoSuchAlgorithmException");
            return "";
        } finally {
            close(in);
        }
    }

    /**
     * 获取字符串SHA 256,512
     *
     * @param data 校验数据
     * @return
     */
    public String encryptSHA(byte[] data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(TYPE_SHA_256);
            byte[] keyByte = key.getBytes();
            byte[] resultByte = new byte[data.length + keyByte.length];
            System.arraycopy(keyByte, 0, resultByte, 0, keyByte.length);
            System.arraycopy(data, 0, resultByte, keyByte.length, data.length);
            md.update(resultByte);
            return bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e("NoSuchAlgorithmException");
            return "";
        }
    }

    /**
     * 一个byte转为2个hex字符
     *
     * @param src byte数组
     * @return 16进制大写字符串
     */
    public String bytes2Hex(byte[] src) {
        // 首先初始化一个字符数组，用来存放每个16进制字符
        final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        // new一个字符数组，这个就是用来组成结果字符串的解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        // 8>>1 等于2的2次方  8<<1 等于2的4次方
        char[] res = new char[src.length << 1];
        int index = 0;
        for (byte b : src) {
            res[index++] = hexDigits[b >>> 4 & 0x0f];
            res[index++] = hexDigits[b & 0x0f];
        }
        return new String(res);
    }
}
