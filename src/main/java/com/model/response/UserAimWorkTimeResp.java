/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: UserAimWorkTimeResp
 * Author:   cretin
 * Date:     1/28/19 19:09
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model.response;

import java.util.Date;
import java.util.List;

/**
 * 〈〉
 *
 * @author cretin
 * @create 1/28/19
 * @since 1.0.0
 */
public class UserAimWorkTimeResp {
    private String userName;
    private String userId;
    private String allTime;
    private List<DingInfo> list;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAllTime() {
        return allTime;
    }

    public void setAllTime(String allTime) {
        this.allTime = allTime;
    }

    public List<DingInfo> getList() {
        return list;
    }

    public void setList(List<DingInfo> list) {
        this.list = list;
    }

    public static class DingInfo {
        private String onTime;
        private String offTime;
        private String todayTime;
        private String stateDesc;
        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getOnTime() {
            return onTime;
        }

        public void setOnTime(String onTime) {
            this.onTime = onTime;
        }

        public String getOffTime() {
            return offTime;
        }

        public void setOffTime(String offTime) {
            this.offTime = offTime;
        }

        public String getTodayTime() {
            return todayTime;
        }

        public void setTodayTime(String todayTime) {
            this.todayTime = todayTime;
        }

        public String getStateDesc() {
            return stateDesc;
        }

        public void setStateDesc(String stateDesc) {
            this.stateDesc = stateDesc;
        }
    }
}