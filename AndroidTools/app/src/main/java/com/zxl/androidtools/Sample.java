package com.zxl.androidtools;

import java.util.Arrays;

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
        System.out.println("原文："+str);
        System.out.println("byte数组"+bs.toString());
        System.out.println("byte数组toString"+Arrays.toString(bs));
    }
}
