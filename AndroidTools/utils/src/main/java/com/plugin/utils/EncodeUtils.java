package com.plugin.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * @Description: 编码解码相关工具类
 * @Author: zxl
 * @Date: 1/9/16 上午10:32.
 */
public class EncodeUtils {
    private static volatile EncodeUtils mInstance = null;

    private EncodeUtils() {
    }

    public static EncodeUtils getInstance() {
        EncodeUtils instance = mInstance;
        if (instance == null) {
            synchronized (EncodeUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new EncodeUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /**
     * 以UTF-8编码字符串
     * <p>若想自己指定字符集,可以使用encode(String string, String charset)方法</p>
     *
     * @param string 要编码的字符
     * @return 编码为UTF-8的字符串
     */
    public String encodeUTF8(String string) {
        return encode(string, "UTF-8");
    }

    /**
     * 字符编码
     * <p>若系统不支持指定的编码字符集,则直接将string原样返回</p>
     *
     * @param string  要编码的字符
     * @param charset 字符集
     * @return 编码为字符集的字符串
     */
    public String encode(String string, String charset) {
        try {
            return URLEncoder.encode(string, charset);
        } catch (UnsupportedEncodingException e) {
            return string;
        }
    }

    /**
     * 以UTF-8解码字符串
     * <p>若想自己指定字符集,可以使用# {decode(String string, String charset)}方法</p>
     *
     * @param string 要解码的字符
     * @return 解码为UTF-8的字符串
     */
    public String decodeUTF8(String string) {
        return decode(string, "UTF-8");
    }

    /**
     * 字符解码
     * <p>若系统不支持指定的解码字符集,则直接将string原样返回</p>
     *
     * @param string  要解码的字符
     * @param charset 字符集
     * @return 解码为字符集的字符串
     */
    public String decode(String string, String charset) {
        try {
            return URLDecoder.decode(string, charset);
        } catch (UnsupportedEncodingException e) {
            return string;
        }
    }
}
