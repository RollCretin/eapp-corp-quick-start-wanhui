package com.mapper;

import com.model.domain.MealLunchList;
import com.model.response.sql.CustomMealLunchList;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 午餐预定详情列表
 */
@Repository
public interface MealLunchListMapper {
    /**
     * 插入一条信息
     *
     * @return
     */
    @Insert( {"insert into t_meal_lunch_list(user_id, month,day,lunch_id,state,year,pay_state) values(#{user_id}, #{month}, #{day}, #{lunch_id}, #{state}, #{year},0)"} )
    int insert(@Param( "user_id" ) String user_id, @Param( "month" ) int month, @Param( "day" ) int day, @Param( "lunch_id" ) int lunch_id, @Param( "state" ) int state, @Param( "year" ) int year);

    /**
     * 获取特定日期所有的数据
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    @Select( {"select * from t_meal_lunch_list where year=#{year} and month=#{month} and day=#{day}"} )
    MealLunchList selectByYearMonthDay(@Param( "year" ) int year, @Param( "month" ) int month, @Param( "day" ) int day);

    /**
     * 获取指定用户id和lunchid对应的数据
     *
     * @param user_id
     * @param lunch_id
     * @return
     */
    @Select( {"select * from t_meal_lunch_list where user_id=#{user_id} and lunch_id=#{lunch_id}"} )
    MealLunchList selectByUserIdLunchId(@Param( "user_id" ) String user_id, @Param( "lunch_id" ) int lunch_id);


    /**
     * 获取指定午餐预定下的所有申请列表
     *
     * @param lunch_id
     * @return
     */
    @Select( {"select tml.*,tu.user_name from t_meal_lunch_list tml,t_user tu where lunch_id = #{lunch_id} and tml.user_id = tu.id order by tml.add_time desc"} )
    List<CustomMealLunchList> selectAllUserByLunchId(@Param( "lunch_id" ) int lunch_id);

    /**
     * 删除
     *
     * @param user_id
     * @param lunch_id
     * @return
     */
    @Delete( {"delete from t_meal_lunch_list where user_id=#{user_id} and lunch_id=#{lunch_id}"} )
    int deleteByUserIdLunchId(@Param( "user_id" ) String user_id, @Param( "lunch_id" ) int lunch_id);

    /**
     * 修改支付状态信息
     * @param user_id
     * @param lunch_id
     * @param pay_state
     * @return
     */
    @Update( {"update t_meal_lunch_list set pay_state = #{pay_state} where lunch_id = #{lunch_id} and user_id = #{user_id}"} )
    int updatePayInfo(@Param( "user_id" ) String user_id, @Param( "lunch_id" ) int lunch_id, @Param( "pay_state" ) int pay_state);
}
