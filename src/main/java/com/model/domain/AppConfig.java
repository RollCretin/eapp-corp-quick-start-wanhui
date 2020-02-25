/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: AppConfig
 * Author:   cretin
 * Date:     12/26/18 17:23
 * Description: 通用配置
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model.domain;

import java.util.Date;

/**
 * 〈通用配置〉
 *
 * @author cretin
 * @create 12/26/18
 * @since 1.0.0
 */
public class AppConfig {
    private int id;
    //餐补每顿的金额
    private int mealMoney;
    //设置算加班的时间点 小时
    private int hour;
    //设置算加班的时间点 分钟
    private int miniute;
    //更新时间
    private Date updateTime;
    //用于配置可额外申请餐补的年月 yyyy-MM
    private String avaiableDate;

    public String getAvaiableDate() {
        return avaiableDate;
    }

    public void setAvaiableDate(String avaiableDate) {
        this.avaiableDate = avaiableDate;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMiniute() {
        return miniute;
    }

    public void setMiniute(int miniute) {
        this.miniute = miniute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMealMoney() {
        return mealMoney;
    }

    public void setMealMoney(int mealMoney) {
        this.mealMoney = mealMoney;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}