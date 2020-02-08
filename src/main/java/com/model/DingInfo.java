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

import java.util.List;

/**
 * 〈打卡统计数据〉
 *
 * @author cretin
 * @create 12/20/18
 * @since 1.0.0
 */
public class DingInfo {
    //用户今日打卡情况
    private List<UserMainInfoModel.DingModel> todayDing;

    //本月迟到次数
    private int lateTimes;

    //本月打卡异常数据
    private int dingErrTimes;

    public List<UserMainInfoModel.DingModel> getTodayDing() {
        return todayDing;
    }

    public void setTodayDing(List<UserMainInfoModel.DingModel> todayDing) {
        this.todayDing = todayDing;
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