package com.zxl.androidtools;

import java.util.Random;

/**
 * @Description:
 * @Author: zxl
 * @Date: 26/8/16.
 */
public class Sample {
    public static void main(String[] args) {


        /**
         * Math.random [0,1)
         *
         * (数据类型)(min+Math.random()*(max-min+1))
         */
        int num1 = (int) (1 + Math.random() * (10 - 1 + 1));//从1到10的int型随数

        /**
         *new Random().nextInt(max) [0,max)
         */
        Random random = new Random();
        int num2 = random.nextInt(10) + 1;//从1到10的int型随数

        long num3 = System.currentTimeMillis();//当前时间毫秒数的long型数字。

    }

}
