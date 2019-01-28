/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: WorkTimeController
 * Author:   cretin
 * Date:     1/28/19 16:37
 * Description: 员工打卡时长统计Controller
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.controller;

import com.mapper.UserMapper;
import com.model.domain.User;
import com.model.response.UserAimWorkTimeResp;
import com.service.WorkTimeService;
import com.util.AccessTokenUtil;
import com.util.ServiceResult;
import com.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 〈员工打卡时长统计Controller〉
 *
 * @author cretin
 * @create 1/28/19
 * @since 1.0.0
 */
@RestController
@RequestMapping( "/worktime" )
@ResponseBody
public class WorkTimeController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkTimeService workTimeService;

    /**
     * 获取所有员工的打卡时长
     */
    @RequestMapping( "all" )
    public ServiceResult getAllUserWorkTime(@RequestParam( value = "year" ) int year,
                                            @RequestParam( value = "month" ) int month,
                                            @RequestParam( value = "names", defaultValue = "" ) String nameArr,
                                            @RequestParam( value = "emails", defaultValue = "" ) String emails) {
        String accessToken = AccessTokenUtil.getToken();
        List<String> names = null;
        if ( !StringUtils.isEmpty(nameArr) ) {
            //用逗号将字符串分开，得到字符串数组
            String[] strs = nameArr.split(",");
            //将字符串数组转换成集合list
            names = Arrays.asList(strs);
        }

        List<User> userList = userMapper.getAllUsers();
        List<User> users = new ArrayList<>();
        if ( names == null || names.isEmpty() ) {
            users.addAll(userList);
        } else {
            for ( User user1 : userList ) {
                if ( names.contains(user1.getUserName()) ) {
                    users.add(user1);
                }
            }
        }
        workTimeService.dataBackup(accessToken, year, month, users,emails);
        return ServiceResult.success("请求已发送，正在处理，预计需要两分钟，请注意收取邮件");
    }


    /**
     * 获取所有员工的打卡时长
     */
    @RequestMapping( "aim" )
    public ServiceResult getSimpleUser(@RequestParam( value = "year" ) int year,
                                       @RequestParam( value = "month" ) int month,
                                       @RequestParam( value = "name", defaultValue = "" ) String name,
                                       @RequestParam( value = "userid", defaultValue = "" ) String userid) {
        String accessToken = AccessTokenUtil.getToken();
        List<User> userList = userMapper.getAllUsers();
        User users = null;
        if ( !StringUtils.isEmpty(name) ) {
            HH:
            for ( User user1 : userList ) {
                if ( name.equals(user1.getUserName()) ) {
                    users = user1;
                    break HH;
                }
            }
        } else if ( !StringUtils.isEmpty(userid) ) {
            HH:
            for ( User user1 : userList ) {
                if ( userid.equals(user1.getId()) ) {
                    users = user1;
                    break HH;
                }
            }
        }
        if ( users != null ) {
            UserAimWorkTimeResp aimData = workTimeService.getAimData(accessToken, year, month, users);
            return ServiceResult.success(aimData);
        } else {
            return ServiceResult.success("未找到对应用户，请修改后重试");
        }
    }
}