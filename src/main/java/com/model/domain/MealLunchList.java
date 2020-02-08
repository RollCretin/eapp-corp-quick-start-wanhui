/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: MealLunch
 * Author:   a112233
 * Date:     2019/12/20 1:50 PM
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model.domain;

import java.util.Date;

/**
 * 〈〉
 *
 * @author a112233
 * @create 2019/12/20
 * @since 1.0.0
 */
public class MealLunchList {
    private int id;
    private String userId;
    private int month;
    private int year;
    private int day;
    private int state;
    private int lunchId;
    //更新时间
    private Date addTime;
    private int payState; //0 未支付 1 已支付

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getLunchId() {
        return lunchId;
    }

    public void setLunchId(int lunchId) {
        this.lunchId = lunchId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}