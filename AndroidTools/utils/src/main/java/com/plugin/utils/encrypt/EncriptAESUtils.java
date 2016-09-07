package com.plugin.utils.encrypt;

import android.os.Parcel;
import android.text.TextUtils;

import com.plugin.utils.bean.LoginUserBean;
import com.plugin.utils.log.LogUtils;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description: AES加密
 * @Author: zxl
 * @Date: 7/9/16 PM5:28.
 */
public class EncriptAESUtils {
    private static final String TYPE_AES = "AES";
    private static final String TYPE_SHA1PRNG = "SHA1PRNG";
    private static final String TYPE_Crypto = "Crypto";
    private static final String TYPE_MODE = "AES/CBC/PKCS5Padding";
    private static final String HEX = "0123456789ABCDEF";
    private static final String MAK = "com.plugin.utils";
    private static volatile EncriptAESUtils mInstance = null;

    private EncriptAESUtils() {
    }

    public static EncriptAESUtils getInstance() {
        EncriptAESUtils instance = mInstance;
        if (instance == null) {
            synchronized (EncriptAESUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new EncriptAESUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }


    /**
     * AES加密
     * 结合密钥生成加密后的密文 返回字符串
     *
     * @param input
     * @return
     * @throws Exception
     */
    public String encrypt2String(byte[] input) {
        return toHex(encrypt2Byte(input));
    }

    /**
     * AES加密
     * 结合密钥生成加密后的密文 返回byte数组
     *
     * @param input
     * @return
     */
    public byte[] encrypt2Byte(byte[] input) {
        byte[] encrypted = null;
        if (input == null) {
            return null;
        }
        encrypted = encrypt(input);
        return encrypted;
    }

    /**
     * 解密后的字符串
     *
     * @return
     */
    public String decrypt2String(String encryptStr) {
        byte[] bytes = decrypt2Byte(encryptStr);
        if (bytes == null) {
            return "";
        } else {
            return new String(bytes);
        }
    }

    /**
     * 解密后的byte[]
     *
     * @param encryptStr
     * @return
     */
    public byte[] decrypt2Byte(String encryptStr) {
        return decrypt(toByte(encryptStr));
    }

    /**
     * 给已经序列化的类加密
     *
     * @param userBean
     * @return
     * @throws Exception
     */
    public String encryptBean(LoginUserBean userBean) {
        if (userBean == null) {
            return "";
        }
        Parcel parcel = Parcel.obtain();
        parcel.setDataPosition(0);
        userBean.writeToParcel(parcel, 0);
        byte[] result = encrypt(parcel.marshall());
        return toHex(result);
    }

    /**
     * 给序列化的类解密
     *
     * @param encrypted
     * @return
     * @throws Exception
     */
    public LoginUserBean decryptBean(String encrypted) {
        if (TextUtils.isEmpty(encrypted)) {
            return null;
        }
        byte[] result = decrypt2Byte(encrypted);
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(result, 0, result.length);
        parcel.setDataPosition(0);
        return LoginUserBean.CREATOR.createFromParcel(parcel);
    }

    /**
     * 加密
     *
     * @param input
     * @return
     */
    private byte[] encrypt(byte[] input) {
        if (input == null) {
            return null;
        }
        try {
            byte[] rawKey = getRawKey(MAK.getBytes());
            SecretKeySpec skeySpec = new SecretKeySpec(rawKey, TYPE_AES);
            Cipher cipher = Cipher.getInstance(TYPE_AES);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return cipher.doFinal(input);
        } catch (Exception e) {
            LogUtils.e("Exception");
            return null;
        }
    }

    /**
     * 解密
     *
     * @param encrypted
     * @return
     */
    private byte[] decrypt(byte[] encrypted) {
        if (encrypted == null) {
            return null;
        }
        try {
            byte[] rawKey = getRawKey(MAK.getBytes());
            SecretKeySpec skeySpec = new SecretKeySpec(rawKey, TYPE_AES);
            Cipher cipher = Cipher.getInstance(TYPE_AES);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            LogUtils.e("Exception");
            return null;
        }
    }

    /**
     * 使用一个安全的随机数来产生一个密匙,密匙加密使用的
     *
     * @param seed
     * @return
     * @throws NoSuchAlgorithmException
     */
    private byte[] getRawKey(byte[] seed) throws NoSuchAlgorithmException, NoSuchProviderException {
        // 获得一个随机数，传入的参数为默认方式。
        SecureRandom sr;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            sr = SecureRandom.getInstance(TYPE_SHA1PRNG, TYPE_Crypto);
        } else {
            sr = SecureRandom.getInstance(TYPE_SHA1PRNG);
        }
        // 设置一个种子,一般是用户设定的密码
        sr.setSeed(seed);
        // 获得一个key生成器（AES加密模式）
        KeyGenerator keyGen = KeyGenerator.getInstance(TYPE_AES);
        // 设置密匙长度128位
        keyGen.init(128, sr);
        // 获得密匙
        SecretKey key = keyGen.generateKey();
        // 返回密匙的byte数组供加解密使用
        byte[] raw = key.getEncoded();
        return raw;
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }


    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}
