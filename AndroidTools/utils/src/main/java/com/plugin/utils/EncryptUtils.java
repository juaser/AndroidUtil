package com.plugin.utils;

import com.plugin.utils.log.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.plugin.utils.IOUtils.close;

/**
 * @Description: 加密相关的工具类
 * @Author: zxl
 * @Date: 1/9/16 上午10:34.
 */
public class EncryptUtils {

    /**
     * MD5加密
     *
     * @param data 明文字符串
     * @return 密文
     */
    public String getMD5(String data) {
        return getMD5(data.getBytes());
    }

    /**
     * MD5加密
     *
     * @param data 明文字符串
     * @param salt 盐
     * @return 密文
     */
    public String getMD5(String data, String salt) {
        return bytes2Hex(encryptMD5((data + salt).getBytes()));
    }

    /**
     * MD5加密
     *
     * @param data 明文字节数组
     * @return 密文
     */
    public String getMD5(byte[] data) {
        return bytes2Hex(encryptMD5(data));
    }

    /**
     * MD5加密
     *
     * @param data 明文字节数组
     * @param salt 盐字节数组
     * @return 密文
     */
    public String getMD5(byte[] data, byte[] salt) {
        byte[] dataSalt = new byte[data.length + salt.length];
        System.arraycopy(data, 0, dataSalt, 0, data.length);
        System.arraycopy(salt, 0, dataSalt, data.length, salt.length);
        return bytes2Hex(encryptMD5(dataSalt));
    }

    /**
     * MD5加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */
    public byte[] encryptMD5(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e("NoSuchAlgorithmException");
        }
        return new byte[0];
    }

    /**
     * 获取文件的MD5校验码
     *
     * @param filePath 文件路径
     * @return 文件的MD5校验码
     */
    public String getMD5File(String filePath) {
        return getMD5File(new File(filePath));
    }

    /**
     * 获取文件的MD5校验码
     *
     * @param file 文件
     * @return 文件的MD5校验码
     */
    public String getMD5File(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            FileChannel channel = in.getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buffer);
            return bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException | IOException e) {
            LogUtils.e("NoSuchAlgorithmException | IOException");
        } finally {
            close(in);
        }
        return "";
    }

    /**
     * SHA加密
     *
     * @param data 明文字符串
     * @return 密文
     */
    public String getSHA(String data) {
        return getSHA(data.getBytes());
    }

    /**
     * SHA加密
     *
     * @param data 明文字节数组
     * @return 密文
     */
    public String getSHA(byte[] data) {
        return bytes2Hex(encryptSHA(data));
    }

    /**
     * SHA加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */
    public byte[] encryptSHA(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e("NoSuchAlgorithmException");
        }
        return new byte[0];
    }

    /**
     * 一个byte转为2个hex字符
     *
     * @param src byte数组
     * @return 16进制大写字符串
     */
    public String bytes2Hex(byte[] src) {
        char[] res = new char[src.length << 1];
        final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        for (int i = 0, j = 0; i < src.length; i++) {
            res[j++] = hexDigits[src[i] >>> 4 & 0x0f];
            res[j++] = hexDigits[src[i] & 0x0f];
        }
        return new String(res);
    }
}
