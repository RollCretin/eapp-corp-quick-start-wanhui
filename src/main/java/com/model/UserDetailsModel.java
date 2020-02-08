package com.model;

import java.util.List;

/**
 * 用户每日打卡详情
 */
public class UserDetailsModel {
    private String month;
    private List<DetailsModel> list;

    //截止今天打卡总时长
    private long allDingTime;
    //截止今天打卡总时长描述
    private String allDingTimeDesc;
    //截止今天平均每日打卡时长
    private long dailyDingTime;
    //截止今天平均每日打卡时长描述
    private String dailyDingTimeDesc;
    //时间差 为正值代表多出的时间 为负值表述总时长不足 缺的时间
    private long mistimingTime;
    //时间差 描述
    private String mistimingTimeDesc;
    //本月总考勤天数
    private int allClockingInDays;
    //已参与考勤天数
    private int hasJoinedDays;
    //打卡异常的天数 包括 请假 补卡 dengdeng
    private int dingErrorDays;

    public int getDingErrorDays() {
        return dingErrorDays;
    }

    public void setDingErrorDays(int dingErrorDays) {
        this.dingErrorDays = dingErrorDays;
    }

    public int getHasJoinedDays() {
        return hasJoinedDays;
    }

    public void setHasJoinedDays(int hasJoinedDays) {
        this.hasJoinedDays = hasJoinedDays;
    }

    public int getAllClockingInDays() {
        return allClockingInDays;
    }

    public void setAllClockingInDays(int allClockingInDays) {
        this.allClockingInDays = allClockingInDays;
    }

    public long getMistimingTime() {
        return mistimingTime;
    }

    public void setMistimingTime(long mistimingTime) {
        this.mistimingTime = mistimingTime;
    }

    public String getMistimingTimeDesc() {
        return mistimingTimeDesc;
    }

    public void setMistimingTimeDesc(String mistimingTimeDesc) {
        this.mistimingTimeDesc = mistimingTimeDesc;
    }

    public long getAllDingTime() {
        return allDingTime;
    }

    public void setAllDingTime(long allDingTime) {
        this.allDingTime = allDingTime;
    }

    public String getAllDingTimeDesc() {
        return allDingTimeDesc;
    }

    public void setAllDingTimeDesc(String allDingTimeDesc) {
        this.allDingTimeDesc = allDingTimeDesc;
    }

    public long getDailyDingTime() {
        return dailyDingTime;
    }

    public void setDailyDingTime(long dailyDingTime) {
        this.dailyDingTime = dailyDingTime;
    }

    public String getDailyDingTimeDesc() {
        return dailyDingTimeDesc;
    }

    public void setDailyDingTimeDesc(String dailyDingTimeDesc) {
        this.dailyDingTimeDesc = dailyDingTimeDesc;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<DetailsModel> getList() {
        return list;
    }

    public void setList(List<DetailsModel> list) {
        this.list = list;
    }

    public static class DetailsModel {
        //打卡状态 0 不正常 1  正常 有上班有下班
        private int status;

        //用户今日打卡情况
        private List<UserMainInfoModel.DingModel> todayDing;

        //用户打卡时长
        private long dingTime;
        //用户打卡时长描述
        private String dingTimeDesc;
        //当前日期
        private String dingDate;
        //设置当前状态  0 正常 标记类型 1 请假 2 外勤 3出差
        private int dingType;

        public int getDingType() {
            return dingType;
        }

        public void setDingType(int dingType) {
            this.dingType = dingType;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<UserMainInfoModel.DingModel> getTodayDing() {
            return todayDing;
        }

        public void setTodayDing(List<UserMainInfoModel.DingModel> todayDing) {
            this.todayDing = todayDing;
        }

        public long getDingTime() {
            return dingTime;
        }

        public void setDingTime(long dingTime) {
            this.dingTime = dingTime;
        }

        public String getDingTimeDesc() {
            return dingTimeDesc;
        }

        public void setDingTimeDesc(String dingTimeDesc) {
            this.dingTimeDesc = dingTimeDesc;
        }

        public String getDingDate() {
            return dingDate;
        }

        public void setDingDate(String dingDate) {
            this.dingDate = dingDate;
        }
    }
}
