package com.plugin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 时间相关的工具类
 * @Author: zxl
 * @Date: 1/9/16 上午11:19.
 */
public class TimeUtils {


    /**
     * <p>在工具类中经常使用到工具类的格式化描述，这个主要是一个日期的操作类，所以日志格式主要使用 SimpleDateFormat的定义格式.</p>
     * 格式的意义如下： 日期和时间模式 <br>
     * <p>日期和时间格式由日期和时间模式字符串指定。在日期和时间模式字符串中，未加引号的字母 'A' 到 'Z' 和 'a' 到 'z'
     * 被解释为模式字母，用来表示日期或时间字符串元素。文本可以使用单引号 (') 引起来，以免进行解释。"''"
     * 表示单引号。所有其他字符均不解释；只是在格式化时将它们简单复制到输出字符串，或者在分析时与输入字符串进行匹配。
     * </p>
     * 定义了以下模式字母（所有其他字符 'A' 到 'Z' 和 'a' 到 'z' 都被保留）： <br>
     * yyyy-MM-dd 1969-12-31
     * yyyy-MM-dd 1970-01-01
     * yyyy-MM-dd HH:mm 1969-12-31 16:00
     * yyyy-MM-dd HH:mm 1970-01-01 00:00
     * yyyy-MM-dd HH:mmZ 1969-12-31 16:00-0800
     * yyyy-MM-dd HH:mmZ 1970-01-01 00:00+0000
     * yyyy-MM-dd HH:mm:ss.SSSZ 1969-12-31 16:00:00.000-0800
     * yyyy-MM-dd HH:mm:ss.SSSZ 1970-01-01 00:00:00.000+0000
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ 1969-12-31T16:00:00.000-0800
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ 1970-01-01T00:00:00.000+0000
     * </pre>
     */
    public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 各时间单位与毫秒的倍数
     */
    public static final int UNIT_MSEC = 1;
    public static final int UNIT_SEC = 1000;
    public static final int UNIT_MIN = 60000;
    public static final int UNIT_HOUR = 3600000;
    public static final int UNIT_DAY = 86400000;

    /**
     * 将时间戳转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param milliseconds 毫秒时间戳
     * @return 时间字符串
     */
    public String milliseconds2String(long milliseconds) {
        return milliseconds2String(milliseconds, DEFAULT_SDF);
    }

    /**
     * 将时间戳转为时间字符串
     * <p>格式为用户自定义</p>
     *
     * @param milliseconds 毫秒时间戳
     * @param format       时间格式
     * @return 时间字符串
     */
    public String milliseconds2String(long milliseconds, SimpleDateFormat format) {
        return format.format(new Date(milliseconds));
    }

    /**
     * 将时间字符串转为时间戳
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 毫秒时间戳
     */
    public long string2Milliseconds(String time) {
        return string2Milliseconds(time, DEFAULT_SDF);
    }

    /**
     * 将时间字符串转为时间戳
     * <p>格式为用户自定义</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return 毫秒时间戳
     */
    public long string2Milliseconds(String time, SimpleDateFormat format) {
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 将时间字符串转为Date类型
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return Date类型
     */
    public Date string2Date(String time) {
        return string2Date(time, DEFAULT_SDF);
    }

    /**
     * 将时间字符串转为Date类型
     * <p>格式为用户自定义</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return Date类型
     */
    public Date string2Date(String time, SimpleDateFormat format) {
        return new Date(string2Milliseconds(time, format));
    }

    /**
     * 将Date类型转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time Date类型时间
     * @return 时间字符串
     */
    public String date2String(Date time) {
        return date2String(time, DEFAULT_SDF);
    }

    /**
     * 将Date类型转为时间字符串
     * <p>格式为用户自定义</p>
     *
     * @param time   Date类型时间
     * @param format 时间格式
     * @return 时间字符串
     */
    public String date2String(Date time, SimpleDateFormat format) {
        return format.format(time);
    }

    /**
     * 将Date类型转为时间戳
     *
     * @param time Date类型时间
     * @return 毫秒时间戳
     */
    public long date2Milliseconds(Date time) {
        return time.getTime();
    }

    /**
     * 将时间戳转为Date类型
     *
     * @param milliseconds 毫秒时间戳
     * @return Date类型时间
     */
    public Date milliseconds2Date(long milliseconds) {
        return new Date(milliseconds);
    }

    /**
     * 毫秒时间戳单位转换（单位：unit）
     *
     * @param milliseconds 毫秒时间戳
     * @param unit         <ul>
     *                     <li>UNIT_MSEC:毫秒</li>
     *                     <li>UNIT_SEC :秒</li>
     *                     <li>UNIT_MIN :分</li>
     *                     <li>UNIT_HOUR:小时</li>
     *                     <li>UNIT_DAY :天</li>
     *                     </ul>
     * @return unit时间戳
     */
    private long milliseconds2Unit(long milliseconds, int unit) {
        switch (unit) {
            case UNIT_MSEC:
            case UNIT_SEC:
            case UNIT_MIN:
            case UNIT_HOUR:
            case UNIT_DAY:
                return Math.abs(milliseconds) / unit;
        }
        return -1;
    }

    /**
     * 获取两个时间差（单位：unit）
     * <p>time1和time2格式都为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time1 时间字符串1
     * @param time2 时间字符串2
     * @param unit  <ul>
     *              <li>UNIT_MSEC:毫秒</li>
     *              <li>UNIT_SEC :秒</li>
     *              <li>UNIT_MIN :分</li>
     *              <li>UNIT_HOUR:小时</li>
     *              <li>UNIT_DAY :天</li>
     *              </ul>
     * @return unit时间戳
     */
    public long getIntervalTime(String time1, String time2, int unit) {
        return getIntervalTime(time1, time2, unit, DEFAULT_SDF);
    }

    /**
     * 获取两个时间差（单位：unit）
     * <p>time1和time2格式都为format</p>
     *
     * @param time1  时间字符串1
     * @param time2  时间字符串2
     * @param unit   <ul>
     *               <li>UNIT_MSEC:毫秒</li>
     *               <li>UNIT_SEC :秒</li>
     *               <li>UNIT_MIN :分</li>
     *               <li>UNIT_HOUR:小时</li>
     *               <li>UNIT_DAY :天</li>
     *               </ul>
     * @param format 时间格式
     * @return unit时间戳
     */
    public long getIntervalTime(String time1, String time2, int unit, SimpleDateFormat format) {
        return milliseconds2Unit(string2Milliseconds(time1, format)
                - string2Milliseconds(time2, format), unit);
    }

    /**
     * 获取两个时间差（单位：unit）
     * <p>time1和time2都为Date类型</p>
     *
     * @param time1 Date类型时间1
     * @param time2 Date类型时间2
     * @param unit  <ul>
     *              <li>UNIT_MSEC:毫秒</li>
     *              <li>UNIT_SEC :秒</li>
     *              <li>UNIT_MIN :分</li>
     *              <li>UNIT_HOUR:小时</li>
     *              <li>UNIT_DAY :天</li>
     *              </ul>
     * @return unit时间戳
     */
    public long getIntervalTime(Date time1, Date time2, int unit) {
        return milliseconds2Unit(date2Milliseconds(time2) - date2Milliseconds(time1), unit);
    }

    /**
     * 获取当前时间
     *
     * @return 毫秒时间戳
     */
    public long getCurTimeMills() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @return 时间字符串
     */
    public String getCurTimeString() {
        return milliseconds2String(getCurTimeMills());
    }

    /**
     * 获取当前时间
     * <p>格式为用户自定义</p>
     *
     * @param format 时间格式
     * @return 时间字符串
     */
    public String getCurTimeString(SimpleDateFormat format) {
        return milliseconds2String(getCurTimeMills(), format);
    }

    /**
     * 获取当前时间
     * <p>Date类型</p>
     *
     * @return Date类型时间
     */
    public Date getCurTimeDate() {
        return new Date();
    }

    /**
     * 获取与当前时间的差（单位：unit）
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @param unit <ul>
     *             <li>UNIT_MSEC:毫秒</li>
     *             <li>UNIT_SEC :秒</li>
     *             <li>UNIT_MIN :分</li>
     *             <li>UNIT_HOUR:小时</li>
     *             <li>UNIT_DAY :天</li>
     *             </ul>
     * @return unit时间戳
     */
    public long getIntervalByNow(String time, int unit) {
        return getIntervalByNow(time, unit, DEFAULT_SDF);
    }

    /**
     * 获取与当前时间的差（单位：unit）
     * <p>time格式为format</p>
     *
     * @param time   时间字符串
     * @param unit   <ul>
     *               <li>UNIT_MSEC:毫秒</li>
     *               <li>UNIT_SEC :秒</li>
     *               <li>UNIT_MIN :分</li>
     *               <li>UNIT_HOUR:小时</li>
     *               <li>UNIT_DAY :天</li>
     *               </ul>
     * @param format 时间格式
     * @return unit时间戳
     */
    public long getIntervalByNow(String time, int unit, SimpleDateFormat format) {
        return getIntervalTime(getCurTimeString(), time, unit, format);
    }

    /**
     * 获取与当前时间的差（单位：unit）
     * <p>time为Date类型</p>
     *
     * @param time Date类型时间
     * @param unit <ul>
     *             <li>UNIT_MSEC:毫秒</li>
     *             <li>UNIT_SEC :秒</li>
     *             <li>UNIT_MIN :分</li>
     *             <li>UNIT_HOUR:小时</li>
     *             <li>UNIT_DAY :天</li>
     *             </ul>
     * @return unit时间戳
     */
    public long getIntervalByNow(Date time, int unit) {
        return getIntervalTime(getCurTimeDate(), time, unit);
    }

    /**
     * 判断闰年
     *
     * @param year 年份
     * @return true: 闰年<br>false: 平年
     */
    public boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    /**
     * 判断两个时间差距
     *
     * @param startDate 秒 时间戳
     * @param endDate   秒 时间戳
     * @return
     */
    public String twoTimestampMinus(int startDate, int endDate) {
        int minus = Math.abs(startDate - endDate);
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        day = minus / (24 * 60 * 60);
        if (day > 0) {
            minus = minus - day * (24 * 60 * 60);
        }
        hour = minus / (60 * 60);
        if (hour > 0) {
            minus = minus - hour * (60 * 60);
        }
        minute = minus / 60;
        if (minute > 0) {
            minus = minus - minute * 60;
        }
        second = minus;
        String str = "";
        if (hour > 0) {
            str = "剩余" + day + "天" + hour + "小时";
        } else if (minute > 0) {
            str = "剩余" + hour + "小时" + minute + "分";
        } else {
            str = "剩余" + minute + "分" + second + "秒";
        }
        return str;
    }
}

