/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: DailyDingInfo
 * Author:   cretin
 * Date:     12/21/18 13:13
 * Description: 今日打卡信息
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model;

import java.util.Date;

/**
 * 〈今日打卡信息〉
 *
 * @author cretin
 * @create 12/21/18
 * @since 1.0.0
 */
public class DailyDingInfo {
    //打卡类型 考勤类型
    //OnDuty：上班
    //OffDuty：下班
    private String dingType;
    //打卡描述
    private String desc;
    //早退 或者迟到 1  未打卡 2
    private int errType;
    //错误类型描述
    private String errTypeDesc;
    //操作时间
    private Date dingTime;

    public String getDingType() {
        return dingType;
    }

    public void setDingType(String dingType) {
        this.dingType = dingType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getErrType() {
        return errType;
    }

    public void setErrType(int errType) {
        this.errType = errType;
    }

    public String getErrTypeDesc() {
        return errTypeDesc;
    }

    public void setErrTypeDesc(String errTypeDesc) {
        this.errTypeDesc = errTypeDesc;
    }

    public Date getDingTime() {
        return dingTime;
    }

    public void setDingTime(Date dingTime) {
        this.dingTime = dingTime;
    }

    @Override
    public boolean equals(Object obj) {
        DailyDingInfo o = ( DailyDingInfo ) obj;
        return o.getDingTime().equals(getDingTime()) &&
                o.getDingType().equals(getDingType()) &&
                o.getErrType() == getErrType() &&
                o.getErrTypeDesc().equals(getErrTypeDesc()) &&
                o.getDesc().equals(getDesc());
    }
}