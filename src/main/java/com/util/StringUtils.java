/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: StringUtils
 * Author:   cretin
 * Date:     12/21/18 15:56
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.util;

/**
 * 〈字符串常用方法〉
 *
 * @author cretin
 * @create 12/21/18
 * @since 1.0.0
 */
public class StringUtils {
    /**
     * 判断是否为空
     * @param text
     * @return
     */
    public static final boolean isEmpty(String text) {
        if ( text == null || "".equals(text) ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取类型 0 正常 1 迟到 早退 2 未打卡
     *
     * @param aimType
     * @return
     */
    public static int getErrType(String aimType) {
        if ( "Normal".equals(aimType) ) {
            //Normal：正常;
            return 0;
        } else if ( "Early".equals(aimType) ) {
            //Early：早退;
            return 1;
        } else if ( "Early".equals(aimType) ) {
            //Early：早退;
            return 1;
        } else if ( "Late".equals(aimType) ) {
            //Late：迟到;
            return 1;
        } else if ( "SeriousLate".equals(aimType) ) {
            //SeriousLate：严重迟到；
            return 1;
        } else if ( "Absenteeism".equals(aimType) ) {
            //Absenteeism：旷工迟到；
            return 1;
        } else if ( "NotSigned".equals(aimType) ) {
            //NotSigned：未打卡\
            return 2;
        }
        return 0;
    }

    /**
     * 获取错误描述
     *
     * @param aimType
     * @return
     */
    public static String getErrDesc(int aimType) {
        if ( aimType == 0 ) {
            return "正常";
        } else if ( aimType == 1 ) {
            return "迟到/早退";
        } else if ( aimType == 2 ) {
            return "未/漏打卡";
        }
        return "";
    }

    /**
     * 获取打卡类型描述
     *
     * @param dingType
     * @return
     */
    public static String getDingTypeDesc(String dingType) {
        if ( "OnDuty".equals(dingType) )
            return "上班打卡";
        else if ( "OffDuty".equals(dingType) ) {
            return "下班打卡";
        }
        return "";
    }
}