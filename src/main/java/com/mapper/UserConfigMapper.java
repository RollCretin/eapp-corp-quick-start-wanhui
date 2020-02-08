package com.mapper;

import com.model.domain.UserConfig;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserConfigMapper {
    /**
     * 查找用户配置
     *
     * @param user_id
     * @return
     */
    @Select( "select * from t_user_config where user_id = #{user_id} and year=#{year} and month=#{month} and day=#{day}" )
    List<UserConfig> findUser(@Param( "user_id" ) String user_id,
                              @Param( "year" ) int year,
                              @Param( "month" ) int month,
                              @Param( "day" ) int day);

    /**
     * 更新数据
     *
     * @param id
     * @param type
     * @return
     */
    @Update( ("update t_user_config set type=#{type} where id=#{id}") )
    int updateUserConfigTypeById(@Param( "id" ) int id, @Param( "type" ) int type);

    /**
     * 查找用户配置
     *
     * @param user_id
     * @return
     */
    @Select( "select * from t_user_config where user_id = #{user_id} and year=#{year} and month = #{month} and type != 0" )
    List<UserConfig> findUserByUserId(@Param( "user_id" ) String user_id, @Param( "year" ) int year, @Param( "month" ) int month);

    /**
     * 插入数据
     *
     * @param user_id
     * @param month
     * @param day
     * @param type
     * @return
     */
    @Insert( {"insert into t_user_config(user_id,year,month,day,type) values(#{user_id},#{year},#{month},#{day},#{type})"} )
    int insertData(@Param( "user_id" ) String user_id, @Param( "year" ) int year, @Param( "month" ) int month, @Param( "day" ) int day, @Param( "type" ) int type);
}
