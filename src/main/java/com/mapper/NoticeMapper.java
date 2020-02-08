package com.mapper;

import com.model.domain.Notice;
import com.model.domain.Remind;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeMapper {

    /**
     * 查找配置
     *
     * @param user_id
     * @return
     */
    @Select( "select * from t_notice where user_id = #{user_id}" )
    Notice findRemindByUserId(@Param( "user_id" ) String user_id);

    /**
     * 查找所有开启通知的配置
     *
     * @return
     */
    @Select( "select * from t_notice where status = 1 and type=#{type}" )
    List<Notice> findAllReminds(@Param( "type" ) int type);

    /**
     * 插入一条信息
     *
     * @param user_id
     * @param status
     * @return
     */
    @Insert( {"insert into t_notice(user_id, status,type) values(#{user_id}, #{status},#{type})"} )
    int insert(@Param( "user_id" ) String user_id, @Param( "status" ) int status, @Param( "type" ) int type);

    /**
     * 更新一条信息
     *
     * @param id
     * @param status
     * @return
     */
    @Insert( {"update t_notice set status=#{status} where id=#{id} and type = #{type} "} )
    int update(@Param( "id" ) int id, @Param( "status" ) int status, @Param( "type" ) int type);
}
