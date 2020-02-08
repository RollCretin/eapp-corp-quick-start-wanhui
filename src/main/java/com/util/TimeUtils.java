package com.util;

import org.joda.time.DateTime;

import java.util.Date;

public class TimeUtils {

    /**
     * 转换时间
     *
     * @param mills
     * @return
     */
    public static String formatTimeToString(long mills) {
        mills = Math.abs(mills);
        int day = ( int ) (mills / (24 * 3600 * 1000l));
        String s = new DateTime(mills + 1514736000000l).toString("HH:mm:ss");
        return formatInt(day * 24 + Integer.parseInt(s.split(":")[0])) + "小时" + s.split(":")[1] + "分钟"
                + s.split(":")[2] + "秒";
    }

    public static String formatInt(int num) {
        return num < 10 ? ("0" + num) : num + "";
    }

    /**
     * 判断两个日期是否是同一天
     *
     * @param date
     * @param date1
     * @return
     */
    public static boolean isSameDay(Date date, Date date1) {
        if ( date != null && date1 != null ) {
            DateTime dateTime = new DateTime(date);
            DateTime dateTime1 = new DateTime(date1);
            if ( dateTime.getYear() == dateTime1.getYear() &&
                    dateTime.getMonthOfYear() == dateTime1.getMonthOfYear() &&
                    dateTime.getDayOfMonth() == dateTime1.getDayOfMonth() ) {
                return true;
            }

        }
        return false;
    }
}
