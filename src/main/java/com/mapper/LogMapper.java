package com.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LogMapper {
    /**
     * 插入一条信息
     *
     * @param user_id
     * @param type
     * @return
     */
    @Insert( {"insert into t_log(user_id, type) values(#{user_id}, #{type})"} )
    int insert(@Param( "user_id" ) String user_id, @Param( "type" ) int type);
}
