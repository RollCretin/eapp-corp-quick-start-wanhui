/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: CustomMealLunch
 * Author:   a112233
 * Date:     2019/12/21 10:23 AM
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model.response.sql;

import java.util.Date;

/**
 * 〈〉
 *
 * @author a112233
 * @create 2019/12/21
 * @since 1.0.0
 */
public class CustomMealLunch {
    private int id;
    private String userId;
    private int month;
    private String userName;
    private String title;
    private int year;
    private int day;
    //更新时间
    private Date addTime;

    private int applyNum;

    public int getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(int applyNum) {
        this.applyNum = applyNum;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}