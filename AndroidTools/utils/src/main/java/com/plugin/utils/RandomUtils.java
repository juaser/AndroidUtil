package com.plugin.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * @Author: zxl
 * @Date: 16-12-10 下午6:41
 * ×@Description:  随机产生的工具类
 */

public class RandomUtils {
    private static volatile RandomUtils mInstance = null;

    private RandomUtils() {
    }

    public static RandomUtils getInstance() {
        RandomUtils instance = mInstance;
        if (instance == null) {
            synchronized (RandomUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new RandomUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /**
     * 两个数之间的随机数[minNum,maxNum] (int)(min+Math.random()*(max-min+1))
     */
    public int randomInt(int minNum, int maxNum) {
        if (maxNum == minNum) {
            return maxNum;
        }
        if (maxNum < minNum) {
            int temp = maxNum;
            maxNum = minNum;
            minNum = temp;
        }
        return (int) (minNum + Math.random() * (maxNum - minNum + 1));
    }

    /**
     * 获得一个随机的颜色
     */
    public int randomColor() {
        Random random = new Random();
        int red = random.nextInt(256);//[0,255)
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return Color.rgb(red, green, blue);
    }
}
