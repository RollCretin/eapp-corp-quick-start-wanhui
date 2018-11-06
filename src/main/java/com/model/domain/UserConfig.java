package com.model.domain;

public class UserConfig {
    private int id;
    private String userId;
    private int month;
    private int day;
    private int type;
    private int year;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        UserConfig o = ( UserConfig ) obj;
        return userId.equals(o.getUserId()) &&
                year == o.getYear() &&
                month == o.getMonth() &&
                day == o.getDay();
    }
}
