/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: MealSupport
 * Author:   cretin
 * Date:     12/21/18 17:35
 * Description: 申请餐补
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model.domain;

import java.util.Date;

/**
 * 〈申请餐补〉
 *
 * @author cretin
 * @create 12/21/18
 * @since 1.0.0
 */
public class MealSupport {
    private int id;
    private String userId;
    private int month;
    private int day;
    private String onduty;
    private String offduty;
    private Date addTime;
    private int year;
    //餐补金额
    private String money;
    //部门名称
    private String departmentName;
    //用户名
    private String userName;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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
}