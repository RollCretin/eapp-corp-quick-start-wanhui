/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: MealLunchListResp
 * Author:   a112233
 * Date:     2019/12/21 9:50 AM
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model.response;

/**
 * 〈〉
 *
 * @author a112233
 * @create 2019/12/21
 * @since 1.0.0
 */
public class MealLunchListResp {
    private String title;
    private String time;
    private String state;
    private int applyNum;
    private String managerName;
    private int lunchId;

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public int getLunchId() {
        return lunchId;
    }

    public void setLunchId(int lunchId) {
        this.lunchId = lunchId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(int applyNum) {
        this.applyNum = applyNum;
    }
}