package com.zxl.androidtools;

import com.plugin.utils.encrypt.Base64;
import com.plugin.utils.encrypt.EncryptRSAUtils;

import java.security.KeyPair;

/**
 * @Description:
 * @Author: zxl
 * @Date: 26/8/16.
 */
public class Sample {
    public static void main(String[] args) {
        String str = "Hello world!";
        // string转byte
        byte[] bs = str.getBytes();
        System.out.println("原文：" + str);
        System.out.println("byte数组" + Base64.encode(bs));
//        System.out.println("byte数组toString" + Arrays.toString(bs));
//        int index = 8;
//        System.out.println("index>>>1----" + (index >>> 4));
//        System.out.println("index<<1----" + (index << 1));
        String sss = "遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去  ";
//        String encodeStr = EncryptMd5Utils.getInstance().encryptMD5(sss);
//        System.out.println("encodeStr----" + encodeStr);
//        System.out.println("decondeStr----"+EncryptMd5Utils.getInstance().get);

        KeyPair keyPair = EncryptRSAUtils.getInstance().generateRSAKeyPair();
        byte[] encode = EncryptRSAUtils.getInstance().encryptData(null, keyPair.getPublic());
        System.out.println("加密:" + Base64.encode(encode));
        System.out.print("解密：" + new String(EncryptRSAUtils.getInstance().decryptData(encode, keyPair.getPrivate())));
//        byte[] decode = EncryptRSAUtils.getInstance().decryptData(encode, keyPair.getPrivate());
//        System.out.println("加密:" + encodeStr);
//        System.out.println("解密：" + decodeStr);
    }

}
