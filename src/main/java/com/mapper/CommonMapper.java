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
import com.model.domain.BlackUser;
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
    @Select( "select count(*) count from t_user_manager where user_id = #{userId} and status = 1" )
    int getUserByUserId(String userId);

    /**
     * 获取不能申请餐补的人员列表
     *
     * @return
     */
    @Select( {"select * from t_user_black where status=1"} )
    List<BlackUser> getAllBlackUser();

    /**
     * 查询当前用户是否在黑名单
     *
     * @param userId
     * @return
     */
    @Select( {"select count(*) from t_user tu,t_user_black tub where tu.id=#{userId} and tu.user_name = tub.user_name and tub.status = 1"} )
    int isUserInBlack(String userId);
}