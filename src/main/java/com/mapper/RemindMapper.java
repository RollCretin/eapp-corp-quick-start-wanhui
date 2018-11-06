package com.mapper;

import com.model.domain.Remind;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RemindMapper {

    /**
     * 查找配置
     *
     * @param user_id
     * @return
     */
    @Select( "select * from t_remind where user_id = #{user_id}" )
    Remind findRemindByUserId(@Param( "user_id" ) String user_id);

    /**
     * 查找所有开启通知的配置
     *
     * @return
     */
    @Select( "select * from t_remind where status = 1" )
    List<Remind> findAllReminds();

    /**
     * 插入一条信息
     *
     * @param user_id
     * @param status
     * @return
     */
    @Insert( {"insert into t_remind(user_id, status) values(#{user_id}, #{status})"} )
    int insert(@Param( "user_id" ) String user_id, @Param( "status" ) int status);

    /**
     * 更新一条信息
     *
     * @param id
     * @param status
     * @return
     */
    @Insert( {"update t_remind set status=#{status} where id=#{id}"} )
    int update(@Param( "id" ) int id, @Param( "status" ) int status);
}
