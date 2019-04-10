package com.message.server.core.util;

import com.message.server.core.exception.BusinessException;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/6/1 09:31
 */
public final class DateUtil {
    /**
     * 格式：年－月－日 小时：分钟：秒
     */
    public static final String FORMAT_ONE = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式：年－月－日 小时：分钟
     */
    public static final String FORMAT_TWO = "yyyy-MM-dd HH:mm";

    /**
     * 格式：年月日 小时分钟秒
     */
    public static final String FORMAT_THREE = "yyyyMMdd-HHmmss";

    /**
     * 格式：年－月－日
     */
    public static final String LONG_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 格式：月－日
     */
    public static final String SHORT_DATE_FORMAT = "MM-dd";

    /**
     * 格式：小时：分钟：秒
     */
    public static final String LONG_TIME_FORMAT = "HH:mm:ss";

    /**
     * 格式：年-月
     */
    public static final String MONTG_DATE_FORMAT = "yyyy-MM";

    /**
     * 格式：年-月
     */
    public static final String MONTG_YEAR_FORMAT = "yy-MM";

    /**
     * 格式：年
     */
    public static final String YEAR_DATE_FORMAT = "yyyy";

    /**
     * 年的加减
     */
    public static final int SUB_YEAR = Calendar.YEAR;

    /**
     * 月加减
     */
    public static final int SUB_MONTH = Calendar.MONTH;

    /**
     * 天的加减
     */
    public static final int SUB_DAY = Calendar.DATE;

    /**
     * 小时的加减
     */
    public static final int SUB_HOUR = Calendar.HOUR;

    /**
     * 分钟的加减
     */
    public static final int SUB_MINUTE = Calendar.MINUTE;

    /**
     * 秒的加减
     */
    public static final int SUB_SECOND = Calendar.SECOND;

    private DateUtil() {
        // 有意留空，不做任何处理
    }

    /**
     * 把符合日期格式的字符串转换为日期类型
     */
    public static Date stringToDate(String dateStr, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setLenient(false);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 把符合日期格式的字符串转换为日期类型
     */
    public static Date stringToDate(String dateStr, String format, ParsePosition pos) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setLenient(false);
        return dateFormat.parse(dateStr, pos);
    }

    /**
     * 把日期转换为字符串
     */
    public static String dateToString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * 获取当前时间的指定格式
     */
    public static String getCurrDate(String format) {
        return dateToString(new Date(), format);
    }

    /**
     * 两个日期相减
     *
     * @return 相减得到的秒数
     */
    public static long timeSub(String firstTime, String secTime) {
        long first = stringToDate(firstTime, FORMAT_ONE).getTime();
        long second = stringToDate(secTime, FORMAT_ONE).getTime();
        return (second - first) / 1000;
    }

    /**
     * 获取某年某月的天数
     */
    public static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得当前日期
     */
    public static int getToday() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获得当前月份
     */
    public static int getToMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得当前年份
     */
    public static int getToYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 返回日期的天
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 返回日期的年
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 返回日期的月份，1-12
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 计算两个日期相差的天数，如果date2 > date1 返回正数，否则返回负数
     */
    public static long dayDiff(Date date1, Date date2) {
        return (date2.getTime() - date1.getTime()) / 86400000;
    }

    /**
     * 比较两个日期的年差
     */
    public static int yearDiff(String before, String after) {
        Date beforeDay = stringToDate(before, LONG_DATE_FORMAT);
        Date afterDay = stringToDate(after, LONG_DATE_FORMAT);
        return getYear(afterDay) - getYear(beforeDay);
    }

    /**
     * 比较指定日期与当前日期的差
     */
    public static int yearDiffCurr(String after) {
        Date beforeDay = new Date();
        Date afterDay = stringToDate(after, LONG_DATE_FORMAT);
        return getYear(beforeDay) - getYear(afterDay);
    }

    /**
     * 获取每月的第一周
     */
    public static int getFirstWeekdayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        // 星期天为第一天
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.set(year, month - 1, 1);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取每月的最后一周
     */
    public static int getLastWeekdayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        // 星期天为第一天
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.set(year, month - 1, getDaysOfMonth(year, month));
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获得当前日期字符串，格式"yyyy-MM-dd HH:mm:ss"
     *
     * @return
     */
    public static String getNow() {
        Calendar today = Calendar.getInstance();
        return dateToString(today.getTime(), FORMAT_ONE);
    }

    /**
     * 获得当前日期，格式"yyyy-MM-dd HH:mm:ss"
     *
     * @return
     */
    public static Date getNowDate() {
        return stringToDate(getNow(), FORMAT_ONE);
    }

    /**
     * 时间戳（毫秒）转时间
     *
     * @param timeStamp 时间戳
     * @return
     */
    public static Date getDateTimeStamp(long timeStamp) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(FORMAT_ONE, Locale.getDefault());
        String sd = timeFormat.format(new Date(timeStamp));
        return stringToDate(sd, FORMAT_ONE);
    }

    /**
     * 获取某年某月最后一天
     *
     * @param date 某年某月时间
     * @return
     */
    public static String getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        SimpleDateFormat timeFormat = new SimpleDateFormat(LONG_DATE_FORMAT, Locale.getDefault());
        return timeFormat.format(calendar.getTime());
    }
}
