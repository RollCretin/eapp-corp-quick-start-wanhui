/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CommonMapper
 * Author:   cretin
 * Date:     12/26/18 17:22
 * Description: 通用配置Mapper
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.mapper;

import com.model.domain.AppConfig;
import com.model.domain.UserManager;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 〈通用配置Mapper〉
 *
 * @author cretin
 * @create 12/26/18
 * @since 1.0.0
 */
@Repository
public interface CommonMapper {

    /**
     * 获取appconfig
     *
     * @return
     */
    @Select( "select * from t_app_config" )
    AppConfig getAppConfig();

    /**
     * 获取接受邮件的所有用户
     *
     * @return
     */
    @Select( "select * from t_user_manager where status = 1" )
    List<UserManager> getAllUserManager();

    /**
     * 判断指定id的用户是否存在
     *
     * @return
     */
    @Select( "select count(*) count from t_user_manager where user_id = #{userId}" )
    int getUserByUserId(String userId);
}