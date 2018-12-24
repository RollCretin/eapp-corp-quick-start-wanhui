/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: MealSupportController
 * Author:   cretin
 * Date:     12/21/18 17:37
 * Description: 餐补对应的Controller
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.controller;

import com.config.Constant;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceListRequest;
import com.dingtalk.api.response.OapiAttendanceListResponse;
import com.model.DailyDingInfo;
import com.model.domain.MealSupport;
import com.model.response.HomeMainResp;
import com.taobao.api.ApiException;
import com.util.AccessTokenUtil;
import com.util.CommRequest;
import com.util.ServiceResult;
import com.util.StringUtils;
import com.util.TimeUtils;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import static com.config.Constant.COMMON_PAGE_NUM;

/**
 * 〈餐补对应的Controller〉
 *
 * @author cretin
 * @create 12/21/18
 * @since 1.0.0
 */
@RestController
@RequestMapping( "/meal" )
@ResponseBody
public class MealSupportController {

    /**
     * 获取可以申请餐补的日期列表
     *
     * @param authCode
     * @param request
     * @return
     */
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServiceResult<HomeMainResp> getMainData(@RequestParam( value = "authCode" ) String authCode,
                                                   HttpServletRequest request) {
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);
        //获取到本月的打卡信息
        Map<String, List<DailyDingInfo>> map = getDingInfo(userId, accessToken);
        List<MealSupport> list = new ArrayList<>();
        for ( Map.Entry<String, List<DailyDingInfo>> entry : map.entrySet() ) {
            List<DailyDingInfo> value = entry.getValue();
            if ( value != null && value.size() == 2 ) {
                DailyDingInfo dailyDingInfo = value.get(0);
                DailyDingInfo dailyDingInfo2 = value.get(1);
                if ( dailyDingInfo.getErrType() == 0
                        && dailyDingInfo2.getErrType() == 0 ) {
                    //没有异常
                    DateTime dateTime = new DateTime(dailyDingInfo2.getDingTime());
                    DateTime aimTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), 20, 0, 0);
                    if ( dateTime.isAfter(aimTime.getMillis()) ) {
                        //满足条件
                        MealSupport mealSupport = new MealSupport();
                        mealSupport.setAddTime(new Date());
                        mealSupport.setDay(dateTime.getDayOfMonth());
                        mealSupport.setMonth(dateTime.getMonthOfYear());
                        mealSupport.setYear(dateTime.getYear());
                        mealSupport.setOffduty(new DateTime(dailyDingInfo.getDingTime()).toString(Constant.DATE_FORMAT));
                        mealSupport.setOnduty(new DateTime(dailyDingInfo2.getDingTime()).toString(Constant.DATE_FORMAT));
                        mealSupport.setUserId(userId);
                        list.add(mealSupport);
                    }
                }
            }
        }

        return null;
    }

    //获取本月打卡信息
    private Map<String, List<DailyDingInfo>> getDingInfo(String userId, String accessToken) {
        Map<String, List<DailyDingInfo>> dailyDingInfoMap = new HashMap<>();
        //每次只能获取到7天数据
        DateTime today = DateTime.now();
        int endDay = today.getDayOfMonth();
        int times = endDay / 7 + (endDay % 7 == 0 ? 0 : 1);
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/list");
        for ( int i = 0; i < times; i++ ) {
            getDingInfoDetail(today.getYear(), today.getMonthOfYear(), endDay, i, 0, userId, accessToken, client, dailyDingInfoMap);
        }
        return dailyDingInfoMap;
    }

    //获取信息
    private void getDingInfoDetail(int year, int month, int lastDay, int start, long offset, String userId, String accessToken, DingTalkClient client, Map<String, List<DailyDingInfo>> daikyList) {
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
            List<OapiAttendanceListResponse.Recordresult> recordresult = response.getRecordresult();
            if ( recordresult != null ) {
                offset += recordresult.size();
                if ( recordresult.size() < COMMON_PAGE_NUM ) {
                    //数据已结束
                    analyse(recordresult, daikyList);
                } else {
                    //数据没有结束
                    analyse(recordresult, daikyList);
                    getDingInfoDetail(year, month, lastDay, start, offset, userId, accessToken, client, daikyList);
                }
            }
        } catch ( ApiException e ) {
            e.printStackTrace();
        }
    }

    private void analyse(List<OapiAttendanceListResponse.Recordresult> recordresultList, Map<String, List<DailyDingInfo>> dailyDingInfoMap) {
        //将数据按天数添加到数组中
        for ( OapiAttendanceListResponse.Recordresult recordresult : recordresultList ) {
            DateTime thisDay = new DateTime(recordresult.getUserCheckTime());
            DailyDingInfo dingInfo = new DailyDingInfo();
            dingInfo.setDingType(recordresult.getCheckType());
            dingInfo.setDingTime(recordresult.getUserCheckTime());
            dingInfo.setErrType(StringUtils.getErrType(recordresult.getTimeResult()));
            dingInfo.setErrTypeDesc(recordresult.getTimeResult());
            //如果是工作日才需要操作
            String key = thisDay.getYear() + "-" + TimeUtils.formatInt(thisDay.getMonthOfYear()) + "-" + TimeUtils.formatInt(thisDay.getDayOfMonth());
            if ( dailyDingInfoMap.containsKey(key) ) {
                List<DailyDingInfo> list = dailyDingInfoMap.get(key);
                list.add(dingInfo);
            } else {
                List<DailyDingInfo> list = new ArrayList<>();
                list.add(dingInfo);
                dailyDingInfoMap.put(key, list);
            }
        }
    }
}