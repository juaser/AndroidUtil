package com.plugin.utils;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

/**
 * @description：
 * @author：zxl
 * @CreateTime 2016/8/12.
 */
public class StringUtils {
    /**
     * 设置字符串中颜色不一致
     */
    public SpannableStringBuilder setTV(String startStr, String centerStr, String footStr, int color) {
        String str = startStr + centerStr + footStr;
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        int start = startStr.length();
        int end = str.length() - footStr.length();
        style.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style;
    }
}
