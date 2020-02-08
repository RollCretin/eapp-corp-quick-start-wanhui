/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CommRequest
 * Author:   cretin
 * Date:     12/21/18 17:43
 * Description: 通用请求
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.util;

import com.config.URLConstant;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceGetusergroupRequest;
import com.dingtalk.api.request.OapiDepartmentGetRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.response.OapiAttendanceGetusergroupResponse;
import com.dingtalk.api.response.OapiDepartmentGetResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.model.response.MealSupportChildResp;
import com.taobao.api.ApiException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 〈通用请求〉
 *
 * @author cretin
 * @create 12/21/18
 * @since 1.0.0
 */
public class CommRequest {
    /**
     * 获取用户姓名
     *
     * @param accessToken
     * @param userId
     * @return
     */
    public static OapiUserGetResponse getUserInfo(String accessToken, String userId) {
        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_USER_GET);
            OapiUserGetRequest request = new OapiUserGetRequest();
            request.setUserid(userId);
            request.setHttpMethod("GET");
            OapiUserGetResponse response = client.execute(request, accessToken);
            return response;
        } catch ( ApiException e ) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取部门信息
     *
     * @param accessToken
     * @param departmentId
     * @return
     */
    public static OapiDepartmentGetResponse getDepartmentInfo(String accessToken, String departmentId) {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/get");
            OapiDepartmentGetRequest request = new OapiDepartmentGetRequest();
            request.setId(departmentId);
            request.setHttpMethod("GET");
            return client.execute(request, accessToken);
        } catch ( ApiException e ) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取考勤组名字
     *
     * @param accessToken
     * @param userId
     * @return
     */

    public static String getKaoqinzu(String accessToken, String userId) {
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getusergroup");
            OapiAttendanceGetusergroupRequest request = new OapiAttendanceGetusergroupRequest();
            request.setUserid(userId);
            OapiAttendanceGetusergroupResponse response = client.execute(request, accessToken);
            if ( response.getResult() == null ) {
                return "获取考勤组失败";
            }
            return response.getResult().getName();
        } catch ( ApiException e ) {
            e.printStackTrace();
            return "获取考勤组失败";
        }
    }

    public static void sort(List<MealSupportChildResp> list) {
        Collections.sort(list, new Comparator<MealSupportChildResp>() {
            @Override
            public int compare(MealSupportChildResp o1, MealSupportChildResp o2) {
                return o2.getDay() - o1.getDay();
            }
        });
    }
}