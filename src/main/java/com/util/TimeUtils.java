package com.util;

import org.joda.time.DateTime;

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
        return formatInt(day * 24 + Integer.parseInt(s.split(":")[0])) + "小时" + s.split(":")[1]+"分钟"
                + s.split(":")[2]+"秒";
    }

    private static String formatInt(int num) {
        return num < 10 ? ("0" + num) : num + "";
    }
}
