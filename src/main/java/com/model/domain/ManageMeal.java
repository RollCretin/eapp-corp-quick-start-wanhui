/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ManageMeal
 * Author:   cretin
 * Date:     12/29/18 11:48
 * Description: 管理员管理餐补申请
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model.domain;

/**
 * 〈管理员管理餐补申请〉
 *
 * @author cretin
 * @create 12/29/18
 * @since 1.0.0
 */
public class ManageMeal {
    private String userId;
    private int year;
    private int month;
    private int times;
    private String userName;
    private int allMoney;

    public int getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(int allMoney) {
        this.allMoney = allMoney;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}