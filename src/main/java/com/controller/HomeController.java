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

import com.config.Constant;
import com.config.DateBean;
import com.config.DateConfig;
import com.config.PicsConfig;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceListRequest;
import com.dingtalk.api.response.OapiAttendanceListResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.mapper.CommonMapper;
import com.mapper.LogMapper;
import com.mapper.RemindMapper;
import com.mapper.UserMapper;
import com.model.DailyDingInfo;
import com.model.DingInfo;
import com.model.UserMainInfoModel;
import com.model.domain.Remind;
import com.model.domain.User;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
    @Autowired
    private RemindMapper remindMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommonMapper commonMapper;

    @RequestMapping( value = "/main", method = RequestMethod.POST )
    public ServiceResult<HomeMainResp> getMainData(@RequestParam( value = "authCode" ) String authCode,
                                                   HttpServletRequest request) {
        HomeMainResp homeMainResp = new HomeMainResp();
        //今日打卡信息
        //获取accessToken,注意正是代码要有异常流处理
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);

        System.out.println("authCode   " + authCode);

        log(2, userId);

        OapiUserGetResponse userInfo = CommRequest.getUserInfo(accessToken, userId);
        if ( userInfo != null && !StringUtils.isEmpty(userInfo.getAvatar()) ) {
            homeMainResp.setAvatar(userInfo.getAvatar());
        } else {
            //头像为空 设置默认头像
            homeMainResp.setAvatar(PicsConfig.getDefaultRandomAvatar());
        }
        homeMainResp.setNickname(userInfo.getName());

        String kaoqinzu = CommRequest.getKaoqinzu(accessToken, userId);
        homeMainResp.setGroupName(kaoqinzu);

        DingInfo dingInfo = getDingInfo(userId, accessToken);
        homeMainResp.setLateTimes(dingInfo.getLateTimes());
        homeMainResp.setDingErrTimes(dingInfo.getDingErrTimes());
        homeMainResp.setTodayDing(dingInfo.getTodayDing());

        //获取是否开启自动提醒功能
        Remind remindByUserId = remindMapper.findRemindByUserId(userId);
        if ( remindByUserId != null ) {
            homeMainResp.setWeekNotice(remindByUserId.getStatus() == 0 ? false : true);
        }

        User userById = userMapper.findUserById(userId);
        if ( userById == null )
            userMapper.insert(userId, userInfo.getName(), userInfo.getAvatar(), kaoqinzu);
        else {
            userMapper.update(userId, userInfo.getName(), userInfo.getAvatar(), kaoqinzu);
        }

        int userByUserId = commonMapper.getUserByUserId(userId);
        if ( userByUserId != 0 ) {
            homeMainResp.setShowNoticeBtn(true);
        }

        return ServiceResult.success(homeMainResp);
    }

    //获取本月打卡信息
    private DingInfo getDingInfo(String userId, String accessToken) {
        DingInfo dingInfo = new DingInfo();
        //早退 或者迟到 1  未打卡 2
        Map<Integer, Integer> days = new HashMap<>();
        //获取今日打卡信息
        List<DailyDingInfo> todayDingInfo = new ArrayList<>();
        //每次只能获取到7天数据
        DateTime today = DateTime.now();
        DateBean monthWorkDay = DateConfig.getDates(today.getYear() + TimeUtils.formatInt(today.getMonthOfYear()));
        int endDay = today.getDayOfMonth();
        int times = endDay / 7 + (endDay % 7 == 0 ? 0 : 1);
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/list");
        for ( int i = 0; i < times; i++ ) {
            getDingInfoDetail(today.getYear(), today.getMonthOfYear(), endDay, i, 0, userId, accessToken, client, monthWorkDay, days, todayDingInfo);
        }

        //用户今日打卡情况
        List<UserMainInfoModel.DingModel> todayDing = new ArrayList<>();

        //组装今日打卡信息
        if ( todayDingInfo != null && !todayDingInfo.isEmpty() ) {
            for ( int i = 0; i < todayDingInfo.size(); i++ ) {
                DailyDingInfo info = todayDingInfo.get(i);
                int type = info.getDingType().equals("OnDuty") ? 0 : 1;
                todayDing.add(new UserMainInfoModel.DingModel(info.getDingTime().getTime(), type, new DateTime(info.getDingTime()).toString(Constant.DATE_FORMAT) + " " + StringUtils.getDingTypeDesc(info.getDingType())));
            }
        }
        Collections.sort(todayDing, new Comparator<UserMainInfoModel.DingModel>() {
            @Override
            public int compare(UserMainInfoModel.DingModel o1, UserMainInfoModel.DingModel o2) {
                return o1.getDingType() - o2.getDingType();
            }
        });
        dingInfo.setTodayDing(todayDing);

        int lateTimes = 0;
        int errTimes = 0;
        //遍历map中的值
        for ( Integer value : days.values() ) {
            if ( value == 1 ) {
                //迟到 早退
                lateTimes++;
            } else if ( value == 2 ) {
                //漏卡 未打卡
                errTimes++;
            }
        }

        dingInfo.setDingErrTimes(errTimes);
        dingInfo.setLateTimes(lateTimes);
        return dingInfo;
    }

    //获取信息
    private void getDingInfoDetail(int year, int month, int lastDay, int start, long offset, String userId, String accessToken, DingTalkClient client, DateBean monthWorkDay, Map<Integer, Integer> days, List<DailyDingInfo> dingInfo) {
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
                    analyse(recordresult, monthWorkDay, days, dingInfo);
                } else {
                    //数据没有结束
                    analyse(recordresult, monthWorkDay, days, dingInfo);
                    getDingInfoDetail(year, month, lastDay, start, offset, userId, accessToken, client, monthWorkDay, days, dingInfo);
                }
            }
        } catch ( ApiException e ) {
            e.printStackTrace();
        }
    }

    private void analyse(List<OapiAttendanceListResponse.Recordresult> recordresultList, DateBean monthWorkDay, Map<Integer, Integer> days, List<DailyDingInfo> dingInfoList) {
        //将数据按天数添加到数组中
        for ( OapiAttendanceListResponse.Recordresult recordresult : recordresultList ) {
            DateTime thisDay = new DateTime(recordresult.getUserCheckTime());
            //如果是工作日才需要操作
            if ( monthWorkDay.isInWork(recordresult.getUserCheckTime()) ) {
                //是今天
                if ( TimeUtils.isSameDay(new Date(), recordresult.getBaseCheckTime()) ) {
                    DailyDingInfo dingInfo = new DailyDingInfo();
                    dingInfo.setDingType(recordresult.getCheckType());
                    dingInfo.setDingTime(recordresult.getUserCheckTime());
                    dingInfo.setErrType(StringUtils.getErrType(recordresult.getTimeResult()));
                    dingInfo.setErrTypeDesc(recordresult.getTimeResult());
                    String desc = StringUtils.getDingTypeDesc(recordresult.getCheckType()) + " " + new DateTime(recordresult.getUserCheckTime()).toString(Constant.DATE_FORMAT + " ")
                            + StringUtils.getErrDesc(dingInfo.getErrType());
                    dingInfo.setDesc(desc);
                    if ( !dingInfoList.contains(dingInfo) )
                        dingInfoList.add(dingInfo);
                } else {
                    String timeResult = recordresult.getTimeResult();
                    if ( "Normal".equals(timeResult) ) {
                        //Normal：正常;
                    } else if ( "Early".equals(timeResult) ) {
                        //Early：早退;
                        if ( !days.containsKey(thisDay.getDayOfMonth()) ) {
                            days.put(thisDay.getDayOfMonth(), 1);
                        }
                    } else if ( "Early".equals(timeResult) ) {
                        //Early：早退;
                        if ( !days.containsKey(thisDay.getDayOfMonth()) ) {
                            days.put(thisDay.getDayOfMonth(), 1);
                        }
                    } else if ( "Late".equals(timeResult) ) {
                        //Late：迟到;
                        if ( !days.containsKey(thisDay.getDayOfMonth()) ) {
                            days.put(thisDay.getDayOfMonth(), 1);
                        }
                    } else if ( "SeriousLate".equals(timeResult) ) {
                        //SeriousLate：严重迟到；
                        if ( !days.containsKey(thisDay.getDayOfMonth()) ) {
                            days.put(thisDay.getDayOfMonth(), 1);
                        }
                    } else if ( "Absenteeism".equals(timeResult) ) {
                        //Absenteeism：旷工迟到；
                        if ( !days.containsKey(thisDay.getDayOfMonth()) ) {
                            days.put(thisDay.getDayOfMonth(), 1);
                        }
                    } else if ( "NotSigned".equals(timeResult) ) {
                        //NotSigned：未打卡
                        days.put(thisDay.getDayOfMonth(), 2);
                    }
                }
            }
        }
    }

    @Autowired
    private LogMapper logMapper;

    //跟踪记录 0 首页 1 统计详情
    private void log(int type, String user_id) {
        logMapper.insert(user_id, type);
    }
}