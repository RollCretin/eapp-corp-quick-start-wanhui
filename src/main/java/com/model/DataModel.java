package com.model;

import java.util.List;

public class DataModel {
    private long startTime;
    private long endTime;
    private String aimTime;
    private List<DingModel> list;
    private long mills;
    private long timeLunchStart;
    private long timeLunchEnd;
    private List<UserMainInfoModel.DingModel> steps;
    private long timeMorningStart;

    public long getTimeMorningStart() {
        return timeMorningStart;
    }

    public void setTimeMorningStart(long timeMorningStart) {
        this.timeMorningStart = timeMorningStart;
    }

    public List<UserMainInfoModel.DingModel> getSteps() {
        return steps;
    }

    public void setSteps(List<UserMainInfoModel.DingModel> steps) {
        this.steps = steps;
    }

    public long getTimeLunchStart() {
        return timeLunchStart;
    }

    public void setTimeLunchStart(long timeLunchStart) {
        this.timeLunchStart = timeLunchStart;
    }

    public long getTimeLunchEnd() {
        return timeLunchEnd;
    }

    public void setTimeLunchEnd(long timeLunchEnd) {
        this.timeLunchEnd = timeLunchEnd;
    }

    public long getMills() {
        return mills;
    }

    public void setMills(long mills) {
        this.mills = mills;
    }

    public String getAimTime() {
        return aimTime;
    }

    public void setAimTime(String aimTime) {
        this.aimTime = aimTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public List<DingModel> getList() {
        return list;
    }

    public void setList(List<DingModel> list) {
        this.list = list;
    }

    public static class DingModel implements Comparable<DingModel> {
        private String type;
        private long time;

        @Override
        public boolean equals(Object obj) {
            DingModel o = ( DingModel ) obj;
            return o.time == time && o.getType().equals(type);
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        @Override
        public int compareTo(DingModel o) {
            return ( int ) (time - o.getTime());
        }
    }
}