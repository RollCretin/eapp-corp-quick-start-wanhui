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
import com.mapper.CommonMapper;
import com.mapper.LogMapper;
import com.mapper.MealSupportMapper;
import com.model.DailyDingInfo;
import com.model.domain.AppConfig;
import com.model.domain.MealSupport;
import com.model.response.MealSupportChildResp;
import com.model.response.MealSupportResp;
import com.taobao.api.ApiException;
import com.util.AccessTokenUtil;
import com.util.ServiceResult;
import com.util.StringUtils;
import com.util.TimeUtils;

import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    private MealSupportMapper mealSupportMapper;

    @Autowired
    private CommonMapper commonMapper;

    public ServiceResult<MealSupportResp> cancel(@RequestParam( value = "authCode" ) String authCode,
                                                 @RequestParam( value = "day" ) int day,
                                                 HttpServletRequest request) {
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);
        DateTime now = DateTime.now();
        log(3, userId);
        MealSupport aimMealSupport = mealSupportMapper.getAimMealSupport(userId, now.getYear(), now.getMonthOfYear(), day);
        List<MealSupport> list = getMonthAvaiableData(userId, accessToken);
        //查询所有的数据
        List<MealSupport> mealSupports = mealSupportMapper.findMealSupport(userId, now.getYear(), now.getMonthOfYear());
        List<MealSupportChildResp> respList = new ArrayList<>();
        for ( MealSupport mealSupport : list ) {
            MealSupportChildResp mealSupportResp = new MealSupportChildResp();
            BeanUtils.copyProperties(mealSupport, mealSupportResp);
            if ( mealSupports != null && !mealSupports.isEmpty() ) {
                HH:
                for ( MealSupport support : mealSupports ) {
                    if ( support.getDay() == mealSupport.getDay() ) {
                        mealSupportResp.setStatus(1);
                        break HH;
                    }
                }
            }
            if ( mealSupport.getDay() == day ) {
                //是今天
                if ( aimMealSupport != null ) {
                    try {
                        mealSupportMapper.deleteOneById(aimMealSupport.getId());
                        mealSupportResp.setStatus(0);
                    } catch ( Exception e ) {
                        return ServiceResult.failure("请求超时，请稍后再试");
                    }
                }
            }
            respList.add(mealSupportResp);
        }
        sort(respList);
        MealSupportResp resp = new MealSupportResp();
        resp.setList(respList);
        resp.setAllMoney(getAllMoney(respList));
        resp.setDate(now.toString("yyyy年MM月"));
        return ServiceResult.success(resp);
    }

    private String getAllMoney(List<MealSupportChildResp> list) {
        AppConfig appConfig = commonMapper.getAppConfig();
        int money = 0;
        for ( MealSupportChildResp mealSupportChildResp : list ) {
            if ( appConfig == null ) {
                mealSupportChildResp.setMoney(20);
            } else {
                mealSupportChildResp.setMoney(appConfig.getMealMoney());
            }
            if ( mealSupportChildResp.getStatus() == 1 ) {
                money += mealSupportChildResp.getMoney();
            }
        }
        return money + "";
    }

    @RequestMapping( value = "/apply", method = RequestMethod.POST )
    public ServiceResult<MealSupportResp> applyOrCancel(@RequestParam( value = "authCode" ) String authCode,
                                                        @RequestParam( value = "day" ) int day,
                                                        @RequestParam( value = "status" ) int status,
                                                        HttpServletRequest request) {
        if ( status == 1 ) {
            //申请
            return apply(authCode, day, request);
        } else {
            //取消
            return cancel(authCode, day, request);
        }
    }

    /**
     * 申请单个日期
     *
     * @param authCode
     * @param request
     * @return
     */
    public ServiceResult<MealSupportResp> apply(@RequestParam( value = "authCode" ) String authCode,
                                                @RequestParam( value = "day" ) int day,
                                                HttpServletRequest request) {
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);
        DateTime now = DateTime.now();
        MealSupport aimMealSupport = mealSupportMapper.getAimMealSupport(userId, now.getYear(), now.getMonthOfYear(), day);
        List<MealSupport> list = getMonthAvaiableData(userId, accessToken);
        //查询所有的数据
        List<MealSupport> mealSupports = mealSupportMapper.findMealSupport(userId, now.getYear(), now.getMonthOfYear());
        List<MealSupportChildResp> respList = new ArrayList<>();
        for ( MealSupport mealSupport : list ) {
            MealSupportChildResp mealSupportResp = new MealSupportChildResp();
            BeanUtils.copyProperties(mealSupport, mealSupportResp);
            if ( mealSupports != null && !mealSupports.isEmpty() ) {
                HH:
                for ( MealSupport support : mealSupports ) {
                    if ( support.getDay() == mealSupport.getDay() ) {
                        mealSupportResp.setStatus(1);
                        break HH;
                    }
                }
            }
            if ( mealSupport.getDay() == day ) {
                //是今天
                if ( aimMealSupport == null ) {
                    aimMealSupport = new MealSupport();
                    BeanUtils.copyProperties(mealSupport, aimMealSupport);
                    try {
                        mealSupportMapper.insertAll(Arrays.asList(aimMealSupport));
                        mealSupportResp.setStatus(1);
                    } catch ( Exception e ) {
                        return ServiceResult.failure("请求超时，请稍后再试");
                    }
                }
            }
            respList.add(mealSupportResp);
        }
        sort(respList);
        MealSupportResp resp = new MealSupportResp();
        resp.setList(respList);
        resp.setAllMoney(getAllMoney(respList));
        resp.setDate(now.toString("yyyy年MM月"));
        return ServiceResult.success(resp);
    }

    /**
     * 一键取消
     *
     * @param authCode
     * @param request
     * @return
     */
    @RequestMapping( value = "/onekey/cancel", method = RequestMethod.POST )
    public ServiceResult<MealSupportResp> oneKeyCancel(@RequestParam( value = "authCode" ) String authCode,
                                                       HttpServletRequest request) {
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);
        DateTime now = DateTime.now();
        try {
            mealSupportMapper.deleteAll(userId, now.getYear(), now.getMonthOfYear());
        } catch ( Exception e ) {
            return ServiceResult.failure("请求超时，请稍后再试");
        }
        //清除所有的数据
        ServiceResult<MealSupportResp> availableData = getAvailableData(authCode, request);
        return availableData;
    }

    /**
     * 一键申请
     *
     * @param authCode
     * @param request
     * @return
     */
    @RequestMapping( value = "/onekey/apply", method = RequestMethod.POST )
    public ServiceResult<MealSupportResp> oneKeyApply(@RequestParam( value = "authCode" ) String authCode,
                                                      HttpServletRequest request) {
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);
        List<MealSupport> list = getMonthAvaiableData(userId, accessToken);
        //查询所有的数据
        DateTime now = DateTime.now();
        List<MealSupport> mealSupports = mealSupportMapper.findMealSupport(userId, now.getYear(), now.getMonthOfYear());
        List<MealSupport> mealSupportResp = new ArrayList<>();
        for ( MealSupport mealSupport : list ) {
            if ( mealSupports != null && !mealSupports.isEmpty() ) {
                boolean has = false;
                HH:
                for ( MealSupport support : mealSupports ) {
                    if ( support.getDay() == mealSupport.getDay() ) {
                        has = true;
                        break HH;
                    }
                }
                if ( !has ) {
                    mealSupportResp.add(mealSupport);
                }
            } else {
                mealSupportResp.add(mealSupport);
            }
        }
        try {
            if ( !mealSupportResp.isEmpty() )
                mealSupportMapper.insertAll(mealSupportResp);
            mealSupports = mealSupportMapper.findMealSupport(userId, now.getYear(), now.getMonthOfYear());
        } catch ( Exception e ) {
            return ServiceResult.failure("请求超时，请稍后再试");
        }
        List<MealSupportChildResp> respList = new ArrayList<>();
        if ( mealSupports != null && !mealSupports.isEmpty() ) {
            for ( MealSupport mealSupport : mealSupports ) {
                MealSupportChildResp mealSupportResp1 = new MealSupportChildResp();
                BeanUtils.copyProperties(mealSupport, mealSupportResp1);
                mealSupportResp1.setStatus(1);
                respList.add(mealSupportResp1);
            }
        }
        sort(respList);
        MealSupportResp resp = new MealSupportResp();
        resp.setList(respList);
        resp.setAllMoney(getAllMoney(respList));
        resp.setDate(now.toString("yyyy年MM月"));
        return ServiceResult.success(resp);
    }

    /**
     * 获取可以申请餐补的日期列表
     *
     * @param authCode
     * @param request
     * @return
     */
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServiceResult<MealSupportResp> getAvailableData(@RequestParam( value = "authCode" ) String authCode,
                                                           HttpServletRequest request) {
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);
        List<MealSupport> list = getMonthAvaiableData(userId, accessToken);
        //查询所有的数据
        DateTime now = DateTime.now();
        List<MealSupport> mealSupports = mealSupportMapper.findMealSupport(userId, now.getYear(), now.getMonthOfYear());
        List<MealSupportChildResp> respList = new ArrayList<>();
        for ( MealSupport mealSupport : list ) {
            MealSupportChildResp mealSupportResp = new MealSupportChildResp();
            BeanUtils.copyProperties(mealSupport, mealSupportResp);
            if ( mealSupports != null && !mealSupports.isEmpty() ) {
                HH:
                for ( MealSupport support : mealSupports ) {
                    if ( support.getDay() == mealSupport.getDay() ) {
                        mealSupportResp.setStatus(1);
                        break HH;
                    }
                }
            }
            respList.add(mealSupportResp);
        }
        sort(respList);
        MealSupportResp resp = new MealSupportResp();
        resp.setList(respList);
        resp.setDate(now.toString("yyyy年MM月"));
        resp.setAllMoney(getAllMoney(respList));
        return ServiceResult.success(resp);
    }

    private List<MealSupport> getMonthAvaiableData(String userId, String accessToken) {
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
                    Date offDutyTime = getOffDutyTime(value);
                    if ( offDutyTime != null ) {
                        DateTime dateTime = new DateTime(offDutyTime);
                        DateTime aimTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), 20, 0, 0);
                        if ( dateTime.isAfter(aimTime.getMillis()) ) {
                            //满足条件
                            MealSupport mealSupport = new MealSupport();
                            mealSupport.setAddTime(new Date());
                            mealSupport.setDay(dateTime.getDayOfMonth());
                            mealSupport.setMonth(dateTime.getMonthOfYear());
                            mealSupport.setYear(dateTime.getYear());
                            mealSupport.setOffduty(new DateTime(getOffDutyTime(value)).toString(Constant.DATE_FORMAT));
                            mealSupport.setOnduty(new DateTime(getOnDutyTime(value)).toString(Constant.DATE_FORMAT));
                            mealSupport.setUserId(userId);
                            list.add(mealSupport);
                        }
                    }
                }
            }
        }
        return list;
    }

    private Date getOffDutyTime(List<DailyDingInfo> value) {
        for ( DailyDingInfo dailyDingInfo : value ) {
            if ( dailyDingInfo.getDingType().equals("OffDuty") ) {
                return dailyDingInfo.getDingTime();
            }
        }
        return null;
    }

    private Date getOnDutyTime(List<DailyDingInfo> value) {
        for ( DailyDingInfo dailyDingInfo : value ) {
            if ( dailyDingInfo.getDingType().equals("OnDuty") ) {
                return dailyDingInfo.getDingTime();
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
            dailyDingInfoMap = getDingInfoDetail(today.getYear(), today.getMonthOfYear(), endDay, i, 0, userId, accessToken, client, dailyDingInfoMap);
        }
        return dailyDingInfoMap;
    }

    //获取信息
    private Map<String, List<DailyDingInfo>> getDingInfoDetail(int year, int month, int lastDay, int start, long offset, String userId, String accessToken, DingTalkClient client, Map<String, List<DailyDingInfo>> daikyList) {
        Map<String, List<DailyDingInfo>> daikyListRes = daikyList;
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
                    daikyListRes = analyse(recordresult, daikyListRes);
                } else {
                    //数据没有结束
                    daikyListRes = analyse(recordresult, daikyListRes);
                    daikyListRes = getDingInfoDetail(year, month, lastDay, start, offset, userId, accessToken, client, daikyListRes);
                }
            }
        } catch ( ApiException e ) {
            e.printStackTrace();
        }
        return daikyListRes;
    }

    private Map<String, List<DailyDingInfo>> analyse(List<OapiAttendanceListResponse.Recordresult> recordresultList, Map<String, List<DailyDingInfo>> dailyDingInfoMap) {
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
        return dailyDingInfoMap;
    }

    private void sort(List<MealSupportChildResp> list) {
        Collections.sort(list, new Comparator<MealSupportChildResp>() {
            @Override
            public int compare(MealSupportChildResp o1, MealSupportChildResp o2) {
                return o2.getDay() - o1.getDay();
            }
        });
    }

    @Autowired
    private LogMapper logMapper;

    //跟踪记录 0 首页 1 统计详情
    private void log(int type, String user_id) {
        logMapper.insert(user_id, type);
    }
}