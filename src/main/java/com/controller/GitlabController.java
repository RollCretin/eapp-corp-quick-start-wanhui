/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: GitlabController
 * Author:   a112233
 * Date:     2019/11/6 4:33 PM
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapper.UserMapper;
import com.model.GitlabHockModel;
import com.model.domain.User;
import com.task.NotifyScheduledService;
import com.util.ServiceResult;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 〈〉
 *
 * @author a112233
 * @create 2019/11/6
 * @since 1.0.0
 */

@RestController
@RequestMapping( "/gitlab" )
@ResponseBody
public class GitlabController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping( value = "/callback" )
    public ServiceResult<String> getMainData(@RequestBody String info) {
        try {
            GitlabHockModel model = objectMapper.readValue(info, GitlabHockModel.class);
            GitlabHockModel.ChangesBean.StateBean state = model.getChanges().getState();
            if ( state == null ) {
                //刚刚发起
                GitlabHockModel.AssigneeBeanX assignee = model.getAssignee();
                String username = assignee.getUsername();
                User user = userMapper.findUserByUserNameEn(username);
                if ( user != null ) {
                    String title = model.getUser().getName() + "邀请你" + model.getEvent_type().toUpperCase();
                    String content = DateTime.now().toString("yyyy-MM-dd HH:mm:ss") + "，" + title + "，标题为：" + model.getObject_attributes().getTitle();
                    NotifyScheduledService.sendCommonMsg("新消息："+title, user.getId(), title, content);
                }
            } else {
                String email = model.getObject_attributes().getLast_commit().getAuthor().getEmail();
                String username = email.substring(0, email.indexOf("@"));
                User user = userMapper.findUserByUserNameEn(username);
                if ( user != null ) {
                    String title = model.getUser().getName() + "进行了" + model.getEvent_type().toUpperCase() + "，当前状态：" + state.getPrevious();
                    String content = DateTime.now().toString("yyyy-MM-dd HH:mm:ss") + "，" + title + "，标题为：" + model.getObject_attributes().getTitle();
                    NotifyScheduledService.sendCommonMsg("新消息："+title, user.getId(), title, content);
                }
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return ServiceResult.success("成功");
    }
}