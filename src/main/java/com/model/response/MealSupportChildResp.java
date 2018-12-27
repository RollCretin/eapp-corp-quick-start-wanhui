/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: MealSupportChildResp
 * Author:   cretin
 * Date:     12/25/18 09:20
 * Description: 可以申请餐补的日期
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model.response;

import com.util.TimeUtils;

import org.joda.time.DateTime;

import java.sql.Time;
import java.util.Date;

/**
 * 〈可以申请餐补的日期〉
 *
 * @author cretin
 * @create 12/25/18
 * @since 1.0.0
 */
public class MealSupportChildResp {
    private int id;
    private String userId;
    private int month;
    private int day;
    private String onduty;
    private String offduty;
    private Date addTime;
    private int year;
    //状态 0 未申请 1 已申请
    private int status;
    //餐补金额
    private int money;
    private String date;

    public String getDate() {
        return year + "-" + TimeUtils.formatInt(month) + "-" + TimeUtils.formatInt(day);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getOnduty() {
        return onduty;
    }

    public void setOnduty(String onduty) {
        this.onduty = onduty;
    }

    public String getOffduty() {
        return offduty;
    }

    public void setOffduty(String offduty) {
        this.offduty = offduty;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}