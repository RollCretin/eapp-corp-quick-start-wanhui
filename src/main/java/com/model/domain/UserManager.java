/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UserManager
 * Author:   cretin
 * Date:     12/26/18 17:25
 * Description: 钉钉管理员
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model.domain;

import java.util.Date;

/**
 * 〈钉钉管理员〉
 *
 * @author cretin
 * @create 12/26/18
 * @since 1.0.0
 */
public class UserManager {
    private int id;
    //用户id
    private String userId;
    //邮件地址
    private String email;
    //用户名
    private String userName;
    private Date updateTime;
    //状态 1 收邮件  0 不收邮件
    private int status;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}