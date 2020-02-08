package com.mapper;

import com.model.domain.MealLunch;
import com.model.response.sql.CustomMealLunch;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 午饭预定列表
 */
@Repository
public interface MealLunchMapper {
    /**
     * 插入一条信息
     *
     * @return
     */
    @Insert( {"insert into t_meal_lunch(user_id, month,day,user_name,title,year) values(#{user_id}, #{month}, #{day}, #{user_name}, #{title}, #{year})"} )
    int insert(@Param( "user_id" ) String user_id, @Param( "month" ) int month, @Param( "day" ) int day,
               @Param( "user_name" ) String user_name, @Param( "title" ) String title, @Param( "year" ) int year);

    /**
     * 获取最新的午饭预定
     * @return
     */
    @Select( {"select * from t_meal_lunch order by id desc limit 1"} )
    MealLunch selectNeweastMealLunch();


    /**
     * 获取所有的预定
     * @return
     */
    @Select( {"select tm.*,count(tml.id) apply_num from t_meal_lunch tm,t_meal_lunch_list tml where tml.lunch_id = tm.id group by tm.id order by tm.id desc"} )
    List<CustomMealLunch> selectAllMealLunch();

    /**
     * 根据id获取
     * @param lunchId
     * @return
     */
    @Select( {"select tm.*,count(tml.id) apply_num from t_meal_lunch tm,t_meal_lunch_list tml where tml.lunch_id = tm.id and tm.id = #{lunch_id} group by tm.id"} )
    CustomMealLunch selectMealLunchById(@Param( "lunch_id" ) int lunchId);
}
