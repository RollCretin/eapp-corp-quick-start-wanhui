/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: MealSupportMapper
 * Author:   cretin
 * Date:     12/25/18 08:53
 * Description: 餐补申请
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mapper;

import com.mapper.provider.MealDAOProvider;
import com.model.domain.MealSupport;
import com.model.domain.MonthTime;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈餐补申请〉
 *
 * @author cretin
 * @create 12/25/18
 * @since 1.0.0
 */
@Repository
public interface MealSupportMapper {
    /**
     * 查找用户所有的餐补信息
     *
     * @param user_id
     * @return
     */
    @Select( "select * from t_meal_supplement where user_id = #{user_id} and year=#{year} and month=#{month}" )
    List<MealSupport> findMealSupport(@Param( "user_id" ) String user_id,
                                      @Param( "year" ) int year,
                                      @Param( "month" ) int month);


    /**
     * 获取本月的所有餐补信息
     * @param year
     * @param month
     * @return
     */
    @Select( "select * from t_meal_supplement where year=#{year} and month=#{month} and month=#{month}" )
    List<MealSupport> findAllUserMealSupport(@Param( "year" ) int year,
                                             @Param( "month" ) int month);

    /**
     * 批量插入数据
     *
     * @param mealSupports
     */
    @InsertProvider( type = MealDAOProvider.class, method = "insertAll" )
    void insertAll(@Param( "list" ) List<MealSupport> mealSupports);

    /**
     * 删除指定的数据
     *
     * @param user_id
     * @param year
     * @param month
     */
    @Delete( "delete from t_meal_supplement where user_id = #{user_id} and year=#{year} and month=#{month}" )
    void deleteAll(@Param( "user_id" ) String user_id,
                   @Param( "year" ) int year,
                   @Param( "month" ) int month);


    /**
     * 删除一个
     *
     * @param id
     */
    @Delete( "delete from t_meal_supplement where id = #{id}" )
    void deleteOneById(@Param( "id" ) int id);

    /**
     * 查询指定日期的数据
     *
     * @param user_id
     * @param year
     * @param month
     * @param day
     * @return
     */
    @Select( "select * from t_meal_supplement where user_id = #{user_id} and year=#{year} and month=#{month} and day=#{day}" )
    MealSupport getAimMealSupport(@Param( "user_id" ) String user_id,
                                  @Param( "year" ) int year,
                                  @Param( "month" ) int month,
                                  @Param( "day" ) int day);
}