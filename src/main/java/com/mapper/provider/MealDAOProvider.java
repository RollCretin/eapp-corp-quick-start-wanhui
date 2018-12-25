package com.mapper.provider;

import com.model.domain.MealSupport;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class MealDAOProvider {
    /**
     * 批量插入
     *
     * @param map
     * @return
     */
    public String insertAll(Map map) {
        List<MealSupport> users = ( List<MealSupport> ) map.get("list");
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO t_meal_supplement ");
        sb.append("(id,user_id, month,day,onduty,offduty,year) ");
        sb.append("VALUES ");
        MessageFormat mf = new MessageFormat("(null, #'{'list[{0}].userId},#'{'list[{0}].month},#'{'list[{0}].day},#'{'list[{0}].onduty}," +
                "#'{'list[{0}].offduty},#'{'list[{0}].year})");
        for ( int i = 0; i < users.size(); i++ ) {
            sb.append(mf.format(new Object[]{i}));
            if ( i < users.size() - 1 ) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
} 