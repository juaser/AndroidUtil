package com.plugin.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @Description: 数字格式化
 * @Author: zxl
 * @Date: 12/9/16 AM10:52.
 */
public class DecimalFormatUtils {
    private static volatile DecimalFormatUtils mInstance = null;

    private DecimalFormatUtils() {
    }

    public static DecimalFormatUtils getInstance() {
        DecimalFormatUtils instance = mInstance;
        if (instance == null) {
            synchronized (DecimalFormatUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new DecimalFormatUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /**
     * Pattern
     * 0 - 如果对应位置上没有数字，则用零代替
     * # - 如果对应位置上没有数字，则保持原样（不用补）；如果最前、后为0，则保持为空。
     * 正负数模板用分号（;）分割
     */
    private String pattern_3comma_2decimal = ",###.##";//每3位中间添加逗号的格式化显示,保留两位有效数字
    private String pattern_permillage = "0.000\u2030";//千分号‰

    /**
     * @description: 自定义数字格式方法
     */
    private String getFormat(String style, BigDecimal value) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(style);// 将格式应用于格式化器
        return df.format(value.doubleValue());
    }

    public String getComma2String(float value) {
        return getFormat(pattern_3comma_2decimal, new BigDecimal(value));
    }

    public String getPermillage(float value) {
        return getFormat(pattern_permillage, new BigDecimal(value));
    }
}
