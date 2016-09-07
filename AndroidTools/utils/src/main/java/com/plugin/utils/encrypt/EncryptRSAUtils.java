package com.plugin.utils.encrypt;

import com.plugin.utils.log.LogUtils;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @Description:
 * @Author: zxl
 * @Date: 7/9/16 PM3:20.
 */
public class EncryptRSAUtils {
    private static volatile EncryptRSAUtils mInstance = null;
    private static final String TYPE_RSA = "RSA";

    private EncryptRSAUtils() {
    }

    public static EncryptRSAUtils getInstance() {
        EncryptRSAUtils instance = mInstance;
        if (instance == null) {
            synchronized (EncryptRSAUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new EncryptRSAUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /**
     * 随机生成RSA密钥对(默认密钥长度为1024)
     *
     * @return
     */
    public KeyPair generateRSAKeyPair() {
        return generateRSAKeyPair(1024);
    }

    /**
     * 随机生成RSA密钥对
     *
     * @param keyLength 密钥长度，范围：512～2048<br>
     *                  一般1024
     * @return
     */
    public KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(TYPE_RSA);
            kpg.initialize(keyLength);
            return kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e("NoSuchAlgorithmException");
            return null;
        }
    }

    /**
     * 用公钥加密 <br>
     * 每次加密的字节数，不能超过密钥的长度值减去11
     *
     * @param data      需加密数据的byte数据
     * @param publicKey 公钥
     * @return 加密后的byte型数据
     */
    public byte[] encryptData(byte[] data, PublicKey publicKey) {
        if (data == null) {
            return null;
        }
        Cipher cipher = null;
        byte[] result = null;
        try {
            cipher = Cipher.getInstance(TYPE_RSA);
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 传入编码数据并返回编码结果
            result = cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e("NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            LogUtils.e("NoSuchPaddingException");
        } catch (BadPaddingException e) {
            LogUtils.e("BadPaddingException");
        } catch (IllegalBlockSizeException e) {
            LogUtils.e("IllegalBlockSizeException");
        } catch (InvalidKeyException e) {
            LogUtils.e("InvalidKeyException");
        }
        return result;
    }

    /**
     * 用私钥解密
     *
     * @param encryptedData 经过encryptedData()加密返回的byte数据
     * @param privateKey    私钥
     * @return
     */
    public byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) {
        if (encryptedData == null) {
            return null;
        }
        Cipher cipher = null;
        byte[] result = null;
        try {
            cipher = Cipher.getInstance(TYPE_RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            result = cipher.doFinal(encryptedData);
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e("NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            LogUtils.e("NoSuchPaddingException");
        } catch (BadPaddingException e) {
            LogUtils.e("BadPaddingException");
        } catch (IllegalBlockSizeException e) {
            LogUtils.e("IllegalBlockSizeException");
        } catch (InvalidKeyException e) {
            LogUtils.e("InvalidKeyException");
        }
        return result;
    }

}

