package com.mapper;

import com.model.domain.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * 查找用户
     *
     * @param user_name_en
     * @return
     */
    @Select( "select * from t_user where user_name_en = #{user_name_en}" )
    User findUserByUserNameEn(@Param( "user_name_en" ) String user_name_en);

    /**
     * 插入一条信息
     *
     * @param id
     * @param user_name
     * @param avatar
     * @param group_name
     * @return
     */
    @Insert( {"insert into t_user(id, user_name, avatar,group_name,phone,user_name_en,email) values(#{id}, #{user_name}, #{avatar}, #{group_name}, #{phone}, #{user_name_en}, #{email})"} )
    int insert(@Param( "id" ) String id, @Param( "user_name" ) String user_name, @Param( "avatar" ) String avatar, @Param( "group_name" ) String group_name
            , @Param( "phone" ) String phone, @Param( "user_name_en" ) String user_name_en,@Param( "email" ) String email);


    /**
     * 更新数据
     *
     * @param user_name
     * @param avatar
     * @param group_name
     * @return
     */
    @Update( {"update t_user set user_name=#{user_name},avatar=#{avatar},group_name=#{group_name},phone=#{phone},user_name_en=#{user_name_en},email=#{email} where id=#{id}"} )
    int update(@Param( "id" ) String id, @Param( "user_name" ) String user_name, @Param( "avatar" ) String avatar, @Param( "group_name" ) String group_name,@Param( "phone" ) String phone, @Param( "user_name_en" ) String user_name_en,@Param( "email" ) String email);

    /**
     * 获取所有用户的id
     *
     * @return
     */
    @Select( {"select id from t_user"} )
    List<String> getAllUserIds();

    /**
     * 获取所有用户信息
     *
     * @return
     */
    @Select( {"select * from t_user"} )
    List<User> getAllUsers();
}