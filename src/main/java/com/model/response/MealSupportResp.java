/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: MealSupportChildResp
 * Author:   cretin
 * Date:     12/25/18 09:20
 * Description: 可以申请餐补的日期
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model.response;

import java.util.List;

/**
 * 〈可以申请餐补的日期〉
 *
 * @author cretin
 * @create 12/25/18
 * @since 1.0.0
 */
public class MealSupportResp {
    //日期
    private String date;
    //已申请money
    private String allMoney;

    public String getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(String allMoney) {
        this.allMoney = allMoney;
    }

    private List<MealSupportChildResp> list;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<MealSupportChildResp> getList() {
        return list;
    }

    public void setList(List<MealSupportChildResp> list) {
        this.list = list;
    }
}