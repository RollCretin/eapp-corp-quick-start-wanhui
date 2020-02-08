package com.mapper;


import com.model.domain.MonthTime;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @date: on 11/9/18
 * @author: cretin
 * @email: mxnzp_life@163.com
 * @desc: 添加描述
 */

@Repository
public interface MonthTimeMapper {
    /**
     * 插入一条信息
     *
     * @param user_id
     * @param month
     * @param year
     * @param all_time
     * @param daily_time
     * @param err_day
     * @param user_type
     * @param status
     * @param clockin_day
     * @param err_days
     * @return
     */
    @Insert( {"insert into t_month_time(user_id, month,year,all_time,daily_time,err_day,user_type,status,clockin_day,err_days) " +
            "values(#{user_id}, #{month}, #{year}, #{all_time}, #{daily_time}, #{err_day}, #{user_type}, #{status}, #{clockin_day},err_days=#{err_days})"} )
    int insert(@Param( "user_id" ) String user_id, @Param( "month" ) int month, @Param( "year" ) int year,
               @Param( "all_time" ) String all_time, @Param( "daily_time" ) String daily_time, @Param( "err_day" ) int err_day,
               @Param( "user_type" ) int user_type, @Param( "status" ) int status, @Param( "clockin_day" ) int clockin_day, @Param("err_days") String err_days);

    /**
     * 查找时长配置
     *
     * @param user_id
     * @return
     */
    @Select( "select * from t_month_time where user_id = #{user_id} and year=#{year} and month=#{month} and user_type=#{user_type}" )
    MonthTime findMonthTimeComfig(@Param( "user_id" ) String user_id,
                                  @Param( "year" ) int year,
                                  @Param( "month" ) int month,
                                  @Param( "user_type" ) int user_type);

    /**
     * 查找所有用户的统计数据
     *
     * @return
     */
    @Select( "select * from t_month_time where year=#{year} and month=#{month}" )
    List<MonthTime> findAllMonthTimeComfig(@Param( "year" ) int year,
                                           @Param( "month" ) int month);

    /**
     * 更新
     *
     * @param user_id
     * @param month
     * @param year
     * @param all_time
     * @param daily_time
     * @param err_day
     * @param user_type
     * @param status
     * @param clockin_day
     * @param err_days
     * @return
     */
    @Update( {"update t_month_time set user_id=#{user_id} , month=#{month} , year=#{year} , " +
            "all_time=#{all_time} , daily_time=#{daily_time} , err_day=#{err_day} , user_type=#{user_type}" +
            " , status=#{status} , clockin_day=#{clockin_day} ,err_days=#{err_days} where id=#{id}"} )
    int update(@Param( "id" ) int id, @Param( "user_id" ) String user_id, @Param( "month" ) int month,
               @Param( "year" ) int year, @Param( "all_time" ) String all_time,
               @Param( "daily_time" ) String daily_time, @Param( "err_day" ) int err_day,
               @Param( "user_type" ) int user_type, @Param( "status" ) int status,
               @Param( "clockin_day" ) int clockin_day, @Param("err_days") String err_days);
}
