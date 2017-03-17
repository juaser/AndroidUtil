package com.plugin.utils;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2016/8/23.
 */

public class ColorUtils {
    private static volatile ColorUtils mInstance = null;

    private ColorUtils() {
    }

    public static ColorUtils getInstance() {
        ColorUtils instance = mInstance;
        if (instance == null) {
            synchronized (ColorUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new ColorUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /**
     * 计算状态栏颜色
     *
     * @param color   color值
     * @param alpha_f alpha值
     * @return 最终的状态栏颜色
     */
    public int calculateStatusColor(int color, float alpha_f) {
        int alpha = color >> 24 & 0xff;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;

        int new_alpha = alpha;
        if (alpha_f > 0 && alpha_f < 1) {
            new_alpha = (int) ((1 - alpha_f) * alpha);
        }
        return new_alpha << 24 | red << 16 | green << 8 | blue;
    }

    public String convertColorInt2String(int color) {
        return Integer.toHexString(color);
    }
}
