package com.plugin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @Description: 时间相关的工具类  此时的时间戳为毫秒数
 * @Author: zxl
 * @Date: 1/9/16 上午11:19.
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
 */
public class TimeUtils {
    private static volatile TimeUtils mInstance = null;
    //默认的输出类型
    public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 各时间单位与毫秒的倍数
     */
    public static final int UNIT_MSEC = 1;
    public static final int UNIT_SEC = 1000;
    public static final int UNIT_MIN = 60000;
    public static final int UNIT_HOUR = 3600000;
    public static final int UNIT_DAY = 86400000;

    private TimeUtils() {
    }

    public static TimeUtils getInstance() {
        TimeUtils instance = mInstance;
        if (instance == null) {
            synchronized (TimeUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new TimeUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /**
     * 毫秒时间戳单位转换（单位：unit） 毫秒转换秒，分，时，天
     */
    private long convertTimeStamp2Unit(long milliseconds, int unit) {
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
     * 将时间戳转为时间字符串 format==null 格式默认 否则 格式自定义
     */
    public String convertTimeStamp2String(long timestamp, SimpleDateFormat format) {
        if (format == null) {
            return DEFAULT_SDF.format(new Date(timestamp));
        }
        return format.format(new Date(timestamp));
    }

    /**
     * 将字符串转为时间戳  format==null 格式为yyyy-MM-dd HH:mm:ss  否则 格式自定义
     */
    public long convertString2TimeStamp(String date, SimpleDateFormat format) {
        try {
            if (format == null) {
                return format.parse(date).getTime();
            }
            return format.parse(date).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 将时间字符串转为Date类型
     */
    public Date convertString2Date(String timeStr, SimpleDateFormat format) {
        return new Date(convertString2TimeStamp(timeStr, format));
    }

    /**
     * 将Date类型转为时间字符串  format==null 格式默认 否则 格式自定义
     */
    public String convertDate2String(Date timeDate, SimpleDateFormat format) {
        if (format == null) {
            return DEFAULT_SDF.format(timeDate);
        }
        return format.format(timeDate);
    }

    /**
     * 将Date类型转为时间戳
     */
    public long convertDate2TimeStamp(Date timeDate) {
        return timeDate.getTime();
    }

    /**
     * 将时间戳转为Date类型
     */
    public Date convertTimeStamp2Date(long timestamp) {
        return new Date(timestamp);
    }

    /**
     * 获取当前的时间戳
     */
    public long getCurrentTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间的字符串
     */
    public String getCurrentString(SimpleDateFormat format) {
        return convertTimeStamp2String(getCurrentTimeStamp(), format);
    }

    /**
     * 获取时间戳timestamp的零点时间的时间戳
     */
    public long getTimeStampZeroTime(long timestamp) {
        return timestamp / UNIT_DAY * UNIT_DAY - TimeZone.getDefault().getRawOffset();
    }

    /**
     * 获取时间戳timestamp的零点时间的时间字符串
     */
    public String getTimeStampZeroTime2String(long timestamp, SimpleDateFormat format) {
        return convertTimeStamp2String(getTimeStampZeroTime(timestamp), format);
    }

    /**
     * 两个时间戳的时间差
     */
    public long getTimeDifference(String timeStr1, String timeStr2, SimpleDateFormat format) {
        return convertString2TimeStamp(timeStr1, format) - convertString2TimeStamp(timeStr2, format);
    }

    /**
     * 两个时间戳相差多少天
     */
    public int getTimeDifference2Day(long timeStamp1, long timeStamp2) {
        //计算两个时间戳的零点差
        long defference = getTimeStampZeroTime(timeStamp1) - getTimeStampZeroTime(timeStamp2);
        return (int) defference / UNIT_DAY;
    }

    /**
     * 判断闰年
     */
    public boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    /**
     * 判断两个时间差距
     *
     * @return
     */
    public String getTimeStampDifference(long startDate, long endDate) {
        long difference = Math.abs(startDate - endDate);
        int day, hour, minute, second = 0;

        day = (int) difference / UNIT_DAY;
        if (day > 0) {
            difference = difference - day * UNIT_DAY;
        }
        hour = (int) difference / UNIT_HOUR;
        if (hour > 0) {
            difference = difference - hour * UNIT_MIN;
        }
        minute = (int) difference / UNIT_MIN;
        if (minute > 0) {
            difference = difference - minute * UNIT_MIN;
        }
        second = (int) difference / UNIT_SEC;
        return day + "天" + hour + "小时" + minute + "分" + second + "秒";
    }

    /**
     * 本周开始时间戳     获取星期日开始时间戳
     */
    public long getWeekSundayStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return getTimeStampZeroTime(convertDate2TimeStamp(cal.getTime()));
    }

    /**
     * 本周结束时间戳  获取星期六结束时间戳
     */
    public long getWeekStaurdayEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return getTimeStampZeroTime(convertDate2TimeStamp(cal.getTime())) + UNIT_DAY - 1;
    }

    /**
     * 本周结束时间戳 - 以星期一为本周的第一天 也就是周日的时间
     */
    public long getWeekMondayEndTime() {
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;// 0是周日，6是周六
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        cal.add(Calendar.DATE, 7 - day_of_week);
        return getTimeStampZeroTime(convertDate2TimeStamp(cal.getTime())) + UNIT_DAY - 1;
    }

    /**
     * 今天星期几
     */
    public String getCurrentWeekDay() {
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;// 0是周日，6是周六
        return getWeekDay(day_of_week);
    }

    /**
     * 获取时间戳对应的星期
     */
    public String converTimeStamp2WeekDay(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(timestamp));
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;// 0是周日，6是周六
        return getWeekDay(day_of_week);
    }

    public String getWeekDay(int day_of_week) {
        String dayStr = "";
        switch (day_of_week) {
            case 0:
                dayStr = "星期日";
                break;
            case 1:
                dayStr = "星期一";
                break;
            case 2:
                dayStr = "星期二";
                break;
            case 3:
                dayStr = "星期三";
                break;
            case 4:
                dayStr = "星期四";
                break;
            case 5:
                dayStr = "星期五";
                break;
            case 6:
                dayStr = "星期六";
                break;
        }
        return dayStr;
    }
}

