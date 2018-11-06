package com.model;

import java.util.List;

/**
 * 用户主要信息
 */
public class UserMainInfoModel {
    private String userName;
    private String avatar;
    //用户今日打卡情况
    private List<DingModel> todayDing;
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
    //满足今天下班打卡时间
    private long meetTodayTtime;
    //满足今天下班打卡时间 描述
    private String meetTodayTtimeDesc;
    //时间差 描述
    private String mistimingTimeDesc;
    //考勤组名字
    private String groupName;
    //今日打卡时长
    private long todayTime;
    //今日打卡时长描述
    private String todayTimeDesc;
    //本月总考勤天数
    private int allClockingInDays;
    //本月剩余考勤天数
    private int leftClockingInDays;
    //剩余考勤时长
    private long leftDingTime;
    //剩余考勤时长描述
    private String leftDingTimeDesc;
    //剩余每天打卡平均时长
    private long leftDailyDingTime;
    //剩余每天打卡平均时长描述
    private String leftDailyDingTimeDesc;
    //打卡异常的天数 包括 请假 补卡 dengdeng
    private int dingErrorDays;
    //今日是否异常
    private int isTodayError;
    //是否开启了自动提醒功能
    private boolean isOpenRemind;

    public boolean isOpenRemind() {
        return isOpenRemind;
    }

    public void setOpenRemind(boolean openRemind) {
        isOpenRemind = openRemind;
    }

    public int getIsTodayError() {
        return isTodayError;
    }

    public void setIsTodayError(int isTodayError) {
        this.isTodayError = isTodayError;
    }

    public int getDingErrorDays() {
        return dingErrorDays;
    }

    public void setDingErrorDays(int dingErrorDays) {
        this.dingErrorDays = dingErrorDays;
    }

    public long getMeetTodayTtime() {
        return meetTodayTtime;
    }

    public void setMeetTodayTtime(long meetTodayTtime) {
        this.meetTodayTtime = meetTodayTtime;
    }

    public String getMeetTodayTtimeDesc() {
        return meetTodayTtimeDesc;
    }

    public void setMeetTodayTtimeDesc(String meetTodayTtimeDesc) {
        this.meetTodayTtimeDesc = meetTodayTtimeDesc;
    }

    public int getAllClockingInDays() {
        return allClockingInDays;
    }

    public void setAllClockingInDays(int allClockingInDays) {
        this.allClockingInDays = allClockingInDays;
    }

    public int getLeftClockingInDays() {
        return leftClockingInDays;
    }

    public void setLeftClockingInDays(int leftClockingInDays) {
        this.leftClockingInDays = leftClockingInDays;
    }

    public long getLeftDingTime() {
        return leftDingTime;
    }

    public void setLeftDingTime(long leftDingTime) {
        this.leftDingTime = leftDingTime;
    }


    public long getLeftDailyDingTime() {
        return leftDailyDingTime;
    }

    public void setLeftDailyDingTime(long leftDailyDingTime) {
        this.leftDailyDingTime = leftDailyDingTime;
    }

    public String getLeftDingTimeDesc() {
        return leftDingTimeDesc;
    }

    public void setLeftDingTimeDesc(String leftDingTimeDesc) {
        this.leftDingTimeDesc = leftDingTimeDesc;
    }

    public String getLeftDailyDingTimeDesc() {
        return leftDailyDingTimeDesc;
    }

    public void setLeftDailyDingTimeDesc(String leftDailyDingTimeDesc) {
        this.leftDailyDingTimeDesc = leftDailyDingTimeDesc;
    }

    public long getTodayTime() {
        return todayTime;
    }

    public void setTodayTime(long todayTime) {
        this.todayTime = todayTime;
    }

    public String getTodayTimeDesc() {
        return todayTimeDesc;
    }

    public void setTodayTimeDesc(String todayTimeDesc) {
        this.todayTimeDesc = todayTimeDesc;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<DingModel> getTodayDing() {
        return todayDing;
    }

    public void setTodayDing(List<DingModel> todayDing) {
        this.todayDing = todayDing;
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

    //包含用户打卡今日的信息
    public static class DingModel {
        public DingModel(long dingTime, int dingType) {
            this.dingTime = dingTime;
            this.dingType = dingType;
        }

        public DingModel(long dingTime, int dingType, String desc) {
            this.dingTime = dingTime;
            this.dingType = dingType;
            this.desc = desc;
        }

        //打卡时间
        private long dingTime;
        //打卡类型 0 上班打卡 1 下班打卡
        private int dingType;
        private String desc;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public long getDingTime() {
            return dingTime;
        }

        public void setDingTime(long dingTime) {
            this.dingTime = dingTime;
        }

        public int getDingType() {
            return dingType;
        }

        public void setDingType(int dingType) {
            this.dingType = dingType;
        }
    }
}
