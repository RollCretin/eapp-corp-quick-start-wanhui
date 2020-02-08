package com.model.domain;

/**
 * @date: on 11/9/18
 * @author: cretin
 * @email: mxnzp_life@163.com
 * @desc: 添加描述
 */
public class MonthTime {
    private int id;
    private String userId;
    private int month;
    private int year;
    //总时长
    private String allTime;
    //每日时长
    private String dailyTime;
    //异常天数
    private int errDay;
    //用户类型
    private int userType;
    //状态 0 时长足够正常 1时长不足
    private int status;
    //截止目前考勤天数
    private int clockinDay;
    //更新时间
    private String updateTime;
    //打卡异常的日期
    private String errDays;

    public String getErrDays() {
        return errDays;
    }

    public void setErrDays(String errDays) {
        this.errDays = errDays;
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

    public String getAllTime() {
        return allTime;
    }

    public void setAllTime(String allTime) {
        this.allTime = allTime;
    }

    public String getDailyTime() {
        return dailyTime;
    }

    public void setDailyTime(String dailyTime) {
        this.dailyTime = dailyTime;
    }

    public int getErrDay() {
        return errDay;
    }

    public void setErrDay(int errDay) {
        this.errDay = errDay;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getClockinDay() {
        return clockinDay;
    }

    public void setClockinDay(int clockinDay) {
        this.clockinDay = clockinDay;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
