package com.mapper;

import com.model.domain.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    /**
     * 查找用户
     *
     * @param id
     * @return
     */
    @Select( "select * from t_user where id = #{id}" )
    User findUserById(@Param( "id" ) String id);

    /**
     * 插入一条信息
     *
     * @param id
     * @param user_name
     * @param avatar
     * @param group_name
     * @return
     */
    @Insert( {"insert into t_user(id, user_name, avatar,group_name) values(#{id}, #{user_name}, #{avatar}, #{group_name})"} )
    int insert(@Param( "id" ) String id, @Param( "user_name" ) String user_name, @Param( "avatar" ) String avatar, @Param( "group_name" ) String group_name);
}