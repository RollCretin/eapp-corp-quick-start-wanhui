/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: UserWorkTimeResp
 * Author:   cretin
 * Date:     1/28/19 17:30
 * Description: 用户打卡信息的回调
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model.response;

/**
 * 〈用户打卡信息的回调〉
 *
 * @author cretin
 * @create 1/28/19
 * @since 1.0.0
 */
public class UserWorkTimeResp {
    private String userId;
    private String userName;
    private String allWorkTime;
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAllWorkTime() {
        return allWorkTime;
    }

    public void setAllWorkTime(String allWorkTime) {
        this.allWorkTime = allWorkTime;
    }

    @Override
    public String toString() {
        return "UserWorkTimeResp{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", allWorkTime='" + allWorkTime + '\'' +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}