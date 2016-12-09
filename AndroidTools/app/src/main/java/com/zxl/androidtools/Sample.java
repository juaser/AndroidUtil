package com.zxl.androidtools;

import java.math.BigDecimal;
import java.util.Random;

/**
 * @Description:
 * @Author: zxl
 * @Date: 26/8/16.
 */
public class Sample {

    public static void main(String[] args) {
//        System.out.println("strfor2size----" + strfor2size(""));
        System.out.println("strfor2size----" + strfor2size("jk"));
        System.out.println("strfor2size----" + strfor2size("jkfj"));
    }

    public static String strfor2size(String str) {
//        if (TextUtils.isEmpty(str)) {
//            return "";
//        }
        int length = str.length();
        if (length == 1 || length == 2) {
            return str;
        }
        //截取第一个字符+"..."+最后一个字符
        return str.substring(0, 1) + "..." + str.substring(length - 1);
    }

    public static int f2int(float f) {
        //传过来的数字是元，现在要转成分 需要乘以100
        BigDecimal b = new BigDecimal(f * 100);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).intValue();
    }

    public static int str2int(String str) {
        try {
            BigDecimal b = new BigDecimal(str);
            return b.multiply(new BigDecimal(100)).intValue();
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int getRandomInt(int max, int min) {
        /**
         *new Random().nextInt(max) [0,max)
         */
        Random random = new Random();
        int num2 = random.nextInt(10) + 1;//从1到10的int型随数
        /**
         * Math.random [0,1)
         *
         * (数据类型)(min+Math.random()*(max-min+1))
         */
        int num1 = (int) (min + Math.random() * (max - min + 1));//从min到max的int型随数
        return num1;
    }
}
