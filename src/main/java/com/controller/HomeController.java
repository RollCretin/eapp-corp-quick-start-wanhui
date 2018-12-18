/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: HomeController
 * Author:   cretin
 * Date:     12/14/18 17:41
 * Description: 负责主页业务逻辑的Controller
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.controller;

import com.config.DateBean;
import com.config.DateConfig;
import com.config.URLConstant;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceListRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiAttendanceListResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.model.DataModel;
import com.taobao.api.ApiException;
import com.util.AccessTokenUtil;
import com.util.ServiceResult;
import com.util.TimeUtils;

import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.config.Constant.COMMON_PAGE_NUM;

/**
 * 〈负责主页业务逻辑的Controller〉
 *
 * @author cretin
 * @create 12/14/18
 * @since 1.0.0
 */
@RestController
@RequestMapping( "/home" )
@ResponseBody
public class HomeController {

    @RequestMapping( value = "/main", method = RequestMethod.POST )
    public ServiceResult getMainData(@RequestParam( value = "authCode" ) String authCode,
                                     HttpServletRequest request) {
        //今日打卡信息
        //获取accessToken,注意正是代码要有异常流处理
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);

        DateTime today = DateTime.now();

        //获取本月需要上班的日期
//        DateBean monthWorkDay = DateConfig.getDates(today.getYear() + TimeUtils.formatInt(today.getDayOfMonth()));

        //每次只能获取到7天数据
        int endDay = today.getDayOfMonth();
        int times = endDay / 7 + (endDay % 7 == 0 ? 0 : 1);
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/list");
        for ( int i = 0; i < times; i++ ) {
            getData(today.getYear(), today.getMonthOfYear(), endDay, i, 0, userId, accessToken, client);
        }


        //本月迟到数据

        //本月打卡异常数据

        return null;
    }

    //获取信息
    private void getData(int year, int month, int lastDay, int start, long offset, String userId, String accessToken, DingTalkClient client) {
        OapiAttendanceListRequest request = new OapiAttendanceListRequest();
        int d = start * 7 + 1;
        DateTime dateTime = new DateTime(year, month, d, 4, 0, 0);
        request.setWorkDateFrom(dateTime.toString("yyyy-MM-dd HH:mm:ss"));
        if ( d + 7 <= lastDay ) {
            DateTime dateTimeNew = dateTime.plusDays(7);
            request.setWorkDateTo(dateTimeNew.toString("yyyy-MM-dd HH:mm:ss"));
        } else {
            DateTime dateTimeNew = dateTime.plusDays(lastDay - d);
            request.setWorkDateTo(dateTimeNew.toString("yyyy-MM-dd HH:mm:ss"));
        }
        request.setUserIdList(Arrays.asList(userId));
        request.setOffset(offset);
        request.setLimit(COMMON_PAGE_NUM);
        try {
            OapiAttendanceListResponse response = client.execute(request, accessToken);
            List<OapiAttendanceListResponse.Recordresult> recordresult1 = response.getRecordresult();
            if ( recordresult1 != null ) {
                offset += recordresult1.size();
                if ( recordresult1.size() < COMMON_PAGE_NUM ) {
                    //数据已结束
//                    analyse(recordresult1);
                } else {
                    //数据没有结束
//                    analyse(recordresult1);
                    getData(year, month, lastDay, start, offset, userId, accessToken, client);
                }
            }
        } catch ( ApiException e ) {
            e.printStackTrace();
        }
    }
}