package com.senon.leanstoragedemo.util.timeselectorutils;

import java.util.Calendar;
import java.util.Date;


public class ZJDateUtils {
    static Calendar calendar = Calendar.getInstance();

    public static Date getDate(int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTime();
    }

    public static Date getDate(int year, int month, int day, int hour, int minute) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 获取年
     *
     * @param date
     * @return
     */
    public static int getYearByDate(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取月
     *
     * @param date
     * @return
     */
    public static int getMonthByDate(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日
     *
     * @param date
     * @return
     */
    public static int getDayByDate(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取时
     *
     * @param date
     * @return
     */
    public static int getHourByDate(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取分
     *
     * @param date
     * @return
     */
    public static int getMinuteByDate(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }
}
