package com.plugin.weight.flowchar;

import android.graphics.Path;
import android.text.TextUtils;
import android.util.SparseArray;

import java.util.ArrayList;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/19 17:12
 */

public class NumberPathManager {

    private static volatile NumberPathManager mInstance = null;

    private NumberPathManager() {
    }

    public static NumberPathManager getInstance() {
        NumberPathManager instance = mInstance;
        if (instance == null) {
            synchronized (NumberPathManager.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new NumberPathManager();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    private SparseArray<float[]> sPointList = null;

    public SparseArray<float[]> getArrayLetter() {
        if (sPointList != null) {
            return sPointList;
        }
        sPointList = new SparseArray<>();
        float[][] nums = new float[][]{NUM_0, NUM_1, NUM_2, NUM_3, NUM_4, NUM_5, NUM_6, NUM_7, NUM_8, NUM_9};
        //ASCII  0-9  0==48
        for (int i = 0, length = nums.length; i < length; i++) {
            sPointList.append(i + 48, nums[i]);
        }
        //'冒号' 点 ASCII  58
        sPointList.append(58, CHAR_COLON);
        return sPointList;
    }

    private float[] NUM_0 = { /* 0*/ 0, 0, 0, 72, 0, 72, 47, 72, 47, 72, 47, 0, 47, 0, 0, 0,};
    private float[] NUM_1 = { /* 1*/ 24, 0, 24, 72,};
    private float[] NUM_2 = { /* 2*/ 0, 0, 47, 0, 47, 0, 47, 36, 47, 36, 0, 36, 0, 36, 0, 72, 0, 72, 47, 72};
    private float[] NUM_3 = { /* 3*/ 0, 0, 47, 0, 47, 0, 47, 36, 47, 36, 0, 36, 47, 36, 47, 72, 47, 72, 0, 72,};
    private float[] NUM_4 = { /* 4*/ 0, 0, 0, 36, 0, 36, 47, 36, 47, 0, 47, 72,};
    private float[] NUM_5 = { /* 5*/ 0, 0, 0, 36, 0, 36, 47, 36, 47, 36, 47, 72, 47, 72, 0, 72, 0, 0, 47, 0};
    private float[] NUM_6 = { /* 6*/ 47, 0, 0, 0, 0, 0, 0, 72, 0, 72, 47, 72, 47, 72, 47, 36, 47, 36, 0, 36};
    private float[] NUM_7 = { /* 7*/ 0, 0, 47, 0, 47, 0, 47, 72};
    private float[] NUM_8 = { /* 8*/ 0, 0, 0, 72, 0, 72, 47, 72, 47, 72, 47, 0, 47, 0, 0, 0, 0, 36, 47, 36};
    private float[] NUM_9 = { /* 9*/ 47, 0, 0, 0, 0, 0, 0, 36, 0, 36, 47, 36, 47, 0, 47, 72, 47, 72, 0, 72};
    private float[] CHAR_COLON = { /*冒号 ':'*/ 24, 20, 24, 32, 24, 40, 24, 52};

    /**
     * @param str              要展示的字符
     * @param scale            缩放比例
     * @param gapBetweenLetter 两个字符之间的宽度
     */
    public ArrayList<float[]> getPathList(String str, float scale, float gapBetweenLetter) {
        ArrayList<float[]> list = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return list;
        }
        getArrayLetter();
        float offsetForWidth = 0;//开始时的x坐标
        for (int i = 0, length = str.length(); i < length; i++) {
            int pos = str.charAt(i);
            int key = sPointList.indexOfKey(pos);
            if (key < 0) {
                //如果字符不存在此列表中，跳过
                continue;
            }
            //存在，取出对应的字符路径 每个四位就是一个笔画
            float[] points = sPointList.get(pos);
            int pointCount = points.length / 4;//每四个数据就是一条线
            for (int count = 0; count < pointCount; count++) {
                float[] pointLine = new float[4];
                //给每个笔画计算具体的位置
                for (int j = 0; j < 4; j++) {
                    float coordinate = points[4 * count + j];//取出对应的每个笔画的数值
                    if (j % 2 == 0) {
                        //x坐标
                        pointLine[j] = (coordinate + offsetForWidth) * scale;//因为要放在一行，所以给每个字符坐标时，要加上当前的x坐标
                    } else {
                        //y坐标
                        pointLine[j] = coordinate * scale;
                    }
                }
                //把每个笔画加入集合里
                list.add(pointLine);
            }
            offsetForWidth += 57 + gapBetweenLetter;//每个字符的间隔
        }
        return list;
    }

    /**
     * 根据集合拼凑路径
     */
    public Path getSrcPath(ArrayList<float[]> pathList) {
        Path path_src = new Path();
        if (pathList == null) {
            return path_src;
        }
        for (int i = 0; i < pathList.size(); i++) {
            float[] floats = pathList.get(i);
            path_src.moveTo(floats[0], floats[1]);
            path_src.lineTo(floats[2], floats[3]);
        }
        return path_src;
    }

    /**
     * 根据集合 拼凑出需要填充的线条路径
     *
     * @param size       每次需要显示的线条数目
     * @param startIndex 开始线条的下标
     * @param isLoop     s是否循环，例如：size=3,pathList.size=10,startIndex=9,
     *                   如果isLoop==false 就只显示一条，true的话就显示三条，就是集合中的9,0,1三条
     */
    public Path getFillPath(ArrayList<float[]> pathList, int size, int startIndex, boolean isLoop) {
        Path path_fill = new Path();
        if (pathList == null || size <= 0 || startIndex >= pathList.size()) {
            return path_fill;
        }
        int pathListSize = pathList.size();
        for (int i = 0; i < size; i++) {
            float[] floats = pathList.get((startIndex + i) % pathListSize);//循环
            path_fill.moveTo(floats[0], floats[1]);
            path_fill.lineTo(floats[2], floats[3]);
        }
        return path_fill;
    }

    /**
     * 计算出路径的最高度
     */
    public float getPathHeigth(ArrayList<float[]> pathList) {
        float maxHeigth = 0;
        for (float[] floats : pathList) {
            for (int i = 0, length = floats.length; i < length; i++) {
                if (i % 2 != 0 && floats[i] > maxHeigth) {
                    //y轴坐标
                    maxHeigth = floats[i];
                }
            }
        }
        return maxHeigth;
    }

    /**
     * 计算出路径的最高度
     */
    public float getPathWidth(ArrayList<float[]> pathList) {
        float maxWidth = 0;
        for (float[] floats : pathList) {
            for (int i = 0, length = floats.length; i < length; i++) {
                if (i % 2 == 0 && floats[i] > maxWidth) {
                    //x轴坐标
                    maxWidth = floats[i];
                }
            }
        }
        return maxWidth;
    }

}
