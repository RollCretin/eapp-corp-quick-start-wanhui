package com.mapper;

import com.model.domain.AccessToken;
import com.model.domain.ManageMeal;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessTokenMapper {
    /**
     * 插入一条信息
     *
     * @param access_token
     * @return
     */
    @Insert( {"insert into t_access_token(access_token) values(#{access_token})"} )
    int insert(@Param( "access_token" ) String access_token);


    /**
     * 更新token
     *
     * @param access_token
     * @param id
     * @return
     */
    @Update( {"update t_access_token set access_token = #{access_token} where id = #{id}"} )
    int update(@Param( "access_token" ) String access_token, @Param( "id" ) int id);

    /**
     * 获取token列表
     *
     * @return
     */
    @Select( "select * from t_access_token order by update_time desc" )
    List<AccessToken> getTokenList();

}
