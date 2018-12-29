/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: BlackUser
 * Author:   cretin
 * Date:     12/29/18 19:16
 * Description: 不能申请餐补的人员model
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model.domain;

/**
 * 〈不能申请餐补的人员model〉
 *
 * @author cretin
 * @create 12/29/18
 * @since 1.0.0
 */
public class BlackUser {
    private int id;
    private String userName;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}