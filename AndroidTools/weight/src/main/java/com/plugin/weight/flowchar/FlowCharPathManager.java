package com.plugin.weight.flowchar;

import android.graphics.Path;
import android.text.TextUtils;
import android.util.SparseArray;

import java.util.ArrayList;

/**
 * @Description: 自定义a-z A-Z 0-9 - . "" 这些字符的路径，每四个数字可以当成两个点的xy坐标
 * @Author: zxl
 * @Date: 2016/12/22 14:07
 */

public class FlowCharPathManager {

    private static volatile FlowCharPathManager mInstance = null;

    private FlowCharPathManager() {
    }

    public static FlowCharPathManager getInstance() {
        FlowCharPathManager instance = mInstance;
        if (instance == null) {
            synchronized (FlowCharPathManager.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new FlowCharPathManager();
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
        float[][] letters = new float[][]{LETTER_A, LETTER_B, LETTER_C, LETTER_D, LETTER_E, LETTER_F, LETTER_G,
                LETTER_H, LETTER_I, LETTER_J, LETTER_K, LETTER_L, LETTER_M, LETTER_N,
                LETTER_O, LETTER_P, LETTER_Q, LETTER_R, LETTER_S, LETTER_T,
                LETTER_U, LETTER_V, LETTER_W, LETTER_X, LETTER_Y, LETTER_Z};
        float[][] nums = new float[][]{NUM_0, NUM_1, NUM_2, NUM_3, NUM_4, NUM_5, NUM_6, NUM_7, NUM_8, NUM_9};
        //ASCII  a-z a==97  A-Z a=65  添加字母的路径
        for (int i = 0, length = letters.length; i < length; i++) {
            sPointList.append(i + 65, letters[i]);
            sPointList.append(i + 97, letters[i]);
        }
        //ASCII  0-9  0==48
        for (int i = 0, length = nums.length; i < length; i++) {
            sPointList.append(i + 48, letters[i]);
        }
        //'' 空字符 ASCII  32
        sPointList.append(32, Char_NONE);
        //'-' 分割线  ASCII  45
        sPointList.append(45, CHAR_SPLITLINE);
        //'.' 点 ASCII  46
        sPointList.append(46, Char_NONE);
        return sPointList;
    }

    /**
     * @param str              要展示的字符
     * @param scale            缩放比例
     * @param gapBetweenLetter 两个字符之间的宽度
     */
    public ArrayList<float[]> getPathList(String str, int scale, int gapBetweenLetter) {
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

    public Path getSrcPath(String mStr, int scale, int gapBetweenLetter) {
        ArrayList<float[]> pathList = getPathList(mStr, scale, gapBetweenLetter);
        Path path_src = new Path();
        for (int i = 0; i < pathList.size(); i++) {
            float[] floats = pathList.get(i);
            path_src.moveTo(floats[0], floats[1]);
            path_src.lineTo(floats[2], floats[3]);
        }
        return path_src;
    }

    private float[] LETTER_A = {
            //A
            24, 0, 1, 22,
            1, 22, 1, 72,
            24, 0, 47, 22,
            47, 22, 47, 72,
            1, 48, 47, 48
    };
    private float[] LETTER_B = {
            //B
            0, 0, 0, 72,
            0, 0, 37, 0,
            37, 0, 47, 11,
            47, 11, 47, 26,
            47, 26, 38, 36,
            38, 36, 0, 36,
            38, 36, 47, 46,
            47, 46, 47, 61,
            47, 61, 38, 71,
            37, 72, 0, 72,
    };
    private float[] LETTER_C = {
            // C
            47, 0, 0, 0,
            0, 0, 0, 72,
            0, 72, 47, 72,
    };
    private float[] LETTER_D = {
            // D
            0, 0, 0, 72,
            0, 0, 24, 0,
            24, 0, 47, 22,
            47, 22, 47, 48,
            47, 48, 23, 72,
            23, 72, 0, 72,
    };
    private float[] LETTER_E = {
            // E
            0, 0, 0, 72,
            0, 0, 47, 0,
            0, 36, 37, 36,
            0, 72, 47, 72,
    };
    private float[] LETTER_F = {
            // F
            0, 0, 0, 72,
            0, 0, 47, 0,
            0, 36, 37, 36,
    };
    private float[] LETTER_G = {
            // G
            47, 23, 47, 0,
            47, 0, 0, 0,
            0, 0, 0, 72,
            0, 72, 47, 72,
            47, 72, 47, 48,
            47, 48, 24, 48,
    };
    private float[] LETTER_H = {
            // H
            0, 0, 0, 72,
            0, 36, 47, 36,
            47, 0, 47, 72,
    };
    private float[] LETTER_I = {
            // I
            0, 0, 47, 0,
            24, 0, 24, 72,
            0, 72, 47, 72,
    };
    private float[] LETTER_J = {
            // J
            47, 0, 47, 72,
            47, 72, 24, 72,
            24, 72, 0, 48,
    };
    private float[] LETTER_K = {
            // K
            0, 0, 0, 72,
            47, 0, 3, 33,
            3, 38, 47, 72,
    };
    private float[] LETTER_L = {
            // L
            0, 0, 0, 72,
            0, 72, 47, 72,
    };
    private float[] LETTER_M = {
            // M
            0, 0, 0, 72,
            0, 0, 24, 23,
            24, 23, 47, 0,
            47, 0, 47, 72,
    };
    private float[] LETTER_N = {
            // N
            0, 0, 0, 72,
            0, 0, 47, 72,
            47, 72, 47, 0,
    };
    private float[] LETTER_O = {
            // O
            0, 0, 0, 72,
            0, 72, 47, 72,
            47, 72, 47, 0,
            47, 0, 0, 0,
    };
    private float[] LETTER_P = {
            // P
            0, 0, 0, 72,
            0, 0, 47, 0,
            47, 0, 47, 36,
            47, 36, 0, 36,
    };
    private float[] LETTER_Q = {
            // Q
            0, 0, 0, 72,
            0, 72, 23, 72,
            23, 72, 47, 48,
            47, 48, 47, 0,
            47, 0, 0, 0,
            24, 28, 47, 71,
    };
    private float[] LETTER_R = {
            // R
            0, 0, 0, 72,
            0, 0, 47, 0,
            47, 0, 47, 36,
            47, 36, 0, 36,
            0, 37, 47, 72,
    };
    private float[] LETTER_S = {
            // S
            47, 0, 0, 0,
            0, 0, 0, 36,
            0, 36, 47, 36,
            47, 36, 47, 72,
            47, 72, 0, 72,
    };
    private float[] LETTER_T = {
            // T
            0, 0, 47, 0,
            24, 0, 24, 72,
    };
    private float[] LETTER_U = {
            // U
            0, 0, 0, 72,
            0, 72, 47, 72,
            47, 72, 47, 0,
    };
    private float[] LETTER_V = {
            // V
            0, 0, 24, 72,
            24, 72, 47, 0,
    };
    private float[] LETTER_W = {
            // W
            0, 0, 0, 72,
            0, 72, 24, 49,
            24, 49, 47, 72,
            47, 72, 47, 0
    };
    private float[] LETTER_X = {
            // X
            0, 0, 47, 72,
            47, 0, 0, 72
    };
    private float[] LETTER_Y = {
            // Y
            0, 0, 24, 23,
            47, 0, 24, 23,
            24, 23, 24, 72
    };
    private float[] LETTER_Z = {
            // Z
            0, 0, 47, 0,
            47, 0, 0, 72,
            0, 72, 47, 72
    };
    private float[] NUM_0 = {
            // 0
            0, 0, 0, 72,
            0, 72, 47, 72,
            47, 72, 47, 0,
            47, 0, 0, 0,
    };
    private float[] NUM_1 = {
            // 1
            24, 0, 24, 72,
    };
    private float[] NUM_2 = {
            // 2
            0, 0, 47, 0,
            47, 0, 47, 36,
            47, 36, 0, 36,
            0, 36, 0, 72,
            0, 72, 47, 72
    };
    private float[] NUM_3 = {
            // 3
            0, 0, 47, 0,
            47, 0, 47, 36,
            47, 36, 0, 36,
            47, 36, 47, 72,
            47, 72, 0, 72,
    };
    private float[] NUM_4 = {
            // 4
            0, 0, 0, 36,
            0, 36, 47, 36,
            47, 0, 47, 72,
    };
    private float[] NUM_5 = {
            // 5
            0, 0, 0, 36,
            0, 36, 47, 36,
            47, 36, 47, 72,
            47, 72, 0, 72,
            0, 0, 47, 0
    };
    private float[] NUM_6 = {
            // 6
            0, 0, 0, 72,
            0, 72, 47, 72,
            47, 72, 47, 36,
            47, 36, 0, 36
    };
    private float[] NUM_7 = {
            // 7
            0, 0, 47, 0,
            47, 0, 47, 72
    };
    private float[] NUM_8 = {
            // 8
            0, 0, 0, 72,
            0, 72, 47, 72,
            47, 72, 47, 0,
            47, 0, 0, 0,
            0, 36, 47, 36
    };
    private float[] NUM_9 = {
            // 9
            47, 0, 0, 0,
            0, 0, 0, 36,
            0, 36, 47, 36,
            47, 0, 47, 72,
    };
    private float[] Char_NONE = {
            //空 ''
    };
    private float[] CHAR_SPLITLINE = {
            //'-'
            0, 36, 47, 36
    };
    private float[] CHAR_POINT = {
            //点 '.'
            24, 60, 24, 72
    };
}
