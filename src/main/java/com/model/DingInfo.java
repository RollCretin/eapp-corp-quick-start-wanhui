/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: DingInfo
 * Author:   cretin
 * Date:     12/20/18 19:12
 * Description: 打卡统计数据
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model;

/**
 * 〈打卡统计数据〉
 *
 * @author cretin
 * @create 12/20/18
 * @since 1.0.0
 */
public class DingInfo {
    //今日打卡信息
    private String todayDingInfo;

    //本月迟到次数
    private int lateTimes;

    //本月打卡异常数据
    private int dingErrTimes;

    public String getTodayDingInfo() {
        return todayDingInfo;
    }

    public void setTodayDingInfo(String todayDingInfo) {
        this.todayDingInfo = todayDingInfo;
    }

    public int getLateTimes() {
        return lateTimes;
    }

    public void setLateTimes(int lateTimes) {
        this.lateTimes = lateTimes;
    }

    public int getDingErrTimes() {
        return dingErrTimes;
    }

    public void setDingErrTimes(int dingErrTimes) {
        this.dingErrTimes = dingErrTimes;
    }
}