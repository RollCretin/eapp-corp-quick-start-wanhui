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
import com.util.CommRequest;
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

import java.sql.Time;
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

    /**
     * 取消申请餐补
     *
     * @param authCode
     * @param day
     * @param request
     * @return
     */
    public ServiceResult<MealSupportResp> cancel(@RequestParam( value = "authCode" ) String authCode,
                                                 @RequestParam( value = "year", defaultValue = "-1" ) int year,
                                                 @RequestParam( value = "month", defaultValue = "-1" ) int month,
                                                 @RequestParam( value = "day" ) int day,
                                                 HttpServletRequest request) {
        if ( year == -1 || month == -1 )
            return ServiceResult.failure("请先选择日期");
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);
        if ( checkUserType(userId) ) {
            //是特殊用户
            return ServiceResult.failure("很抱歉，您所在的班制不支持在线申请餐补，详情请联系行政部门");
        }
        log(3, userId);
        MealSupport aimMealSupport = mealSupportMapper.getAimMealSupport(userId, year, month, day);
        AppConfig appConfig = commonMapper.getAppConfig();
        int hour = 21;
        int minute = 0;
        if ( appConfig != null ) {
            hour = appConfig.getHour();
            minute = appConfig.getMiniute();
        }
        List<MealSupport> list = getMonthAvaiableData(userId, accessToken, hour, minute, year, month);
        //查询所有的数据
        List<MealSupport> mealSupports = mealSupportMapper.findMealSupport(userId, year, month);
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
        CommRequest.sort(respList);
        MealSupportResp resp = new MealSupportResp();
        resp.setList(respList);
        resp.setAllMoney(getAllMoney(respList));
        resp.setDate(TimeUtils.formatInt(year) + "-" + TimeUtils.formatInt(month));
        return ServiceResult.success(resp, "取消成功");
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

    /**
     * 申请指定的餐补
     *
     * @param authCode
     * @param day
     * @param status
     * @param request
     * @return
     */
    @RequestMapping( value = "/apply", method = RequestMethod.POST )
    public ServiceResult<MealSupportResp> applyOrCancel(@RequestParam( value = "authCode" ) String authCode,
                                                        @RequestParam( value = "year", defaultValue = "-1" ) int year,
                                                        @RequestParam( value = "month", defaultValue = "-1" ) int month,
                                                        @RequestParam( value = "day" ) int day,
                                                        @RequestParam( value = "status" ) int status,
                                                        HttpServletRequest request) {
        if ( status == 1 ) {
            //申请
            return apply(authCode, year, month, day, request);
        } else {
            //取消
            return cancel(authCode, year, month, day, request);
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
                                                @RequestParam( value = "year", defaultValue = "-1" ) int year,
                                                @RequestParam( value = "month", defaultValue = "-1" ) int month,
                                                @RequestParam( value = "day" ) int day,
                                                HttpServletRequest request) {
        if ( year == -1 || month == -1 )
            return ServiceResult.failure("请先选择日期");
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);
        if ( checkUserType(userId) ) {
            //是特殊用户
            return ServiceResult.failure("很抱歉，您所在的班制不支持在线申请餐补，详情请联系行政部门");
        }
        MealSupport aimMealSupport = mealSupportMapper.getAimMealSupport(userId, year, month, day);
        AppConfig appConfig = commonMapper.getAppConfig();
        int hour = 21;
        int minute = 0;
        if ( appConfig != null ) {
            hour = appConfig.getHour();
            minute = appConfig.getMiniute();
        }
        List<MealSupport> list = getMonthAvaiableData(userId, accessToken, hour, minute, year, month);
        //查询所有的数据
        List<MealSupport> mealSupports = mealSupportMapper.findMealSupport(userId, year, month);
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
        CommRequest.sort(respList);
        MealSupportResp resp = new MealSupportResp();
        resp.setList(respList);
        resp.setAllMoney(getAllMoney(respList));
        resp.setDate(TimeUtils.formatInt(year) + "-" + TimeUtils.formatInt(month));
        return ServiceResult.success(resp, "申请成功");
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
                                                       @RequestParam( value = "year", defaultValue = "-1" ) int year,
                                                       @RequestParam( value = "month", defaultValue = "-1" ) int month,
                                                       HttpServletRequest request) {
        if ( year == -1 || month == -1 )
            return ServiceResult.failure("请先选择日期");
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);
        if ( checkUserType(userId) ) {
            //是特殊用户
            return ServiceResult.failure("很抱歉，您所在的班制不支持在线申请餐补，详情请联系行政部门");
        }
        return oneKeyCancelByUserId(userId, year, month, request);
    }

    @RequestMapping( value = "/onekey/cancel/userId", method = RequestMethod.POST )
    public ServiceResult<MealSupportResp> oneKeyCancelByUserId(@RequestParam( value = "userId" ) String userId,
                                                               @RequestParam( value = "year", defaultValue = "-1" ) int year,
                                                               @RequestParam( value = "month", defaultValue = "-1" ) int month,
                                                               HttpServletRequest request) {
        if ( year == -1 || month == -1 )
            return ServiceResult.failure("请先选择日期");
        if ( checkUserType(userId) ) {
            //是特殊用户
            return ServiceResult.failure("很抱歉，您所在的班制不支持在线申请餐补，详情请联系行政部门");
        }
        try {
            mealSupportMapper.deleteAll(userId, year, month);
        } catch ( Exception e ) {
            return ServiceResult.failure("请求超时，请稍后再试");
        }
        //清除所有的数据
        ServiceResult<MealSupportResp> availableData = getAvailableDataByUserId(userId, year, month, "一键取消成功", request);
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
                                                      @RequestParam( value = "year", defaultValue = "-1" ) int year,
                                                      @RequestParam( value = "month", defaultValue = "-1" ) int month,
                                                      HttpServletRequest request) {
        if ( year == -1 || month == -1 )
            return ServiceResult.failure("请先选择日期");
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);
        if ( checkUserType(userId) ) {
            //是特殊用户
            return ServiceResult.failure("很抱歉，您所在的班制不支持在线申请餐补，详情请联系行政部门");
        }
        return oneKeyApplyByUserId(userId, year, month, request);
    }

    /**
     * 一键申请
     *
     * @param userId
     * @param request
     * @return
     */
    @RequestMapping( value = "/onekey/apply/userId", method = RequestMethod.POST )
    public ServiceResult<MealSupportResp> oneKeyApplyByUserId(@RequestParam( value = "userId" ) String userId,
                                                              @RequestParam( value = "year", defaultValue = "-1" ) int year,
                                                              @RequestParam( value = "month", defaultValue = "-1" ) int month,
                                                              HttpServletRequest request) {
        if ( year == -1 || month == -1 )
            return ServiceResult.failure("请先选择日期");
        String accessToken = AccessTokenUtil.getToken();
        if ( checkUserType(userId) ) {
            //是特殊用户
            return ServiceResult.failure("很抱歉，您所在的班制不支持在线申请餐补，详情请联系行政部门");
        }
        AppConfig appConfig = commonMapper.getAppConfig();
        int hour = 21;
        int minute = 0;
        if ( appConfig != null ) {
            hour = appConfig.getHour();
            minute = appConfig.getMiniute();
        }
        List<MealSupport> list = getMonthAvaiableData(userId, accessToken, hour, minute, year, month);
        //查询所有的数据
        List<MealSupport> mealSupports = mealSupportMapper.findMealSupport(userId, year, month);
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
            mealSupports = mealSupportMapper.findMealSupport(userId, year, month);
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
        CommRequest.sort(respList);
        MealSupportResp resp = new MealSupportResp();
        resp.setList(respList);
        resp.setAllMoney(getAllMoney(respList));
        resp.setDate(TimeUtils.formatInt(year) + "-" + TimeUtils.formatInt(month));
        if ( respList.isEmpty() ) {
            return ServiceResult.success(resp, "暂无可用数据");
        } else {
            return ServiceResult.success(resp, "一键申请成功");
        }
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
                                                           @RequestParam( value = "year", defaultValue = "-1" ) int year,
                                                           @RequestParam( value = "month", defaultValue = "-1" ) int month,
                                                           String message,
                                                           HttpServletRequest request) {
        if ( StringUtils.isEmpty(message) )
            message = "";
        if ( year == -1 || month == -1 ) {
            DateTime now = DateTime.now();
            year = now.getYear();
            month = now.getMonthOfYear();
        }
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);
        return getAvailableDataByUserId(userId, year, month, message, request);
    }

    @RequestMapping( value = "/list/userId", method = RequestMethod.POST )
    public ServiceResult<MealSupportResp> getAvailableDataByUserId(@RequestParam( value = "userId" ) String userId,
                                                                   @RequestParam( value = "year", defaultValue = "-1" ) int year,
                                                                   @RequestParam( value = "month", defaultValue = "-1" ) int month,
                                                                   String message,
                                                                   HttpServletRequest request) {
        if ( StringUtils.isEmpty(message) )
            message = "";
        if ( year == -1 || month == -1 ) {
            DateTime now = DateTime.now();
            year = now.getYear();
            month = now.getMonthOfYear();
        }
        MealSupportResp resp = new MealSupportResp();
        String accessToken = AccessTokenUtil.getToken();
        resp.setShowHistory(true);
        List<String> availableDate = new ArrayList<>();
        //说明是第一次进来
        DateTime today = DateTime.now();
        if ( today.isAfter(new DateTime(today.getYear(), today.getMonthOfYear(), 5, 10, 0, 0)) ) {
            //过了次月的5号上午10点 不能在申请了
            resp.setShowHistory(false);
            availableDate.add(TimeUtils.formatInt(today.getYear()) + "-" + TimeUtils.formatInt(today.getMonthOfYear()));
        } else {
            availableDate.add(TimeUtils.formatInt(today.getYear()) + "-" + TimeUtils.formatInt(today.getMonthOfYear()));
            today = today.minusMonths(1);
            availableDate.add(TimeUtils.formatInt(today.getYear()) + "-" + TimeUtils.formatInt(today.getMonthOfYear()));
        }
        AppConfig appConfig = commonMapper.getAppConfig();
        if ( appConfig != null ) {
            //在这里判断下有没有需要额外配置的月份 用于配置可额外申请餐补的年月 yyyy-MM
            if ( !StringUtils.isEmpty(appConfig.getAvaiableDate()) ) {
                String[] items = appConfig.getAvaiableDate().split(" ");
                for ( String item : items ) {
                    String[] date = item.split("-");
                    if ( date.length == 2 ) {
                        int y = Integer.parseInt(date[0]);
                        int d = Integer.parseInt(date[1]);
                        String res = TimeUtils.formatInt(y) + "-" + TimeUtils.formatInt(d);
                        if ( !availableDate.contains(res) ) {
                            availableDate.add(res);
                        }
                    }
                }
            }
        }
        resp.setAvailableDate(availableDate);
        if ( checkUserType(userId) ) {
            //是特殊用户
            return ServiceResult.failure("很抱歉，您所在的班制不支持在线申请餐补，详情请联系行政部门");
        }
        int hour = 21;
        int minute = 0;
        if ( appConfig != null ) {
            hour = appConfig.getHour();
            minute = appConfig.getMiniute();
        }
        List<MealSupport> list = getMonthAvaiableData(userId, accessToken, hour, minute, year, month);
        //查询所有的数据
        List<MealSupport> mealSupports = mealSupportMapper.findMealSupport(userId, year, month);
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
        CommRequest.sort(respList);

        Collections.sort(availableDate, (o1, o2) -> {
            String[] s1 = o1.split("-");
            String[] s2 = o2.split("-");
            if ( Integer.parseInt(s1[0]) != Integer.parseInt(s2[0]) ) {
                return -(Integer.parseInt(s1[0]) - Integer.parseInt(s2[0]));
            } else {
                return -(Integer.parseInt(s1[1]) - Integer.parseInt(s2[1]));
            }
        });

        resp.setList(respList);
        resp.setDate(TimeUtils.formatInt(year) + "-" + TimeUtils.formatInt(month));
        resp.setAllMoney(getAllMoney(respList));
        if ( respList.isEmpty() ) {
            return ServiceResult.success(resp, "暂无可用数据");
        } else {
            return ServiceResult.success(resp, message);
        }
    }

    private List<MealSupport> getMonthAvaiableData(String userId, String accessToken, int hour, int minute, int year, int month) {
        //获取到本月的打卡信息
        Map<String, List<DailyDingInfo>> map = getDingInfo(userId, accessToken, year, month);
        List<MealSupport> list = new ArrayList<>();
        for ( Map.Entry<String, List<DailyDingInfo>> entry : map.entrySet() ) {
            List<DailyDingInfo> value = entry.getValue();
            if ( value != null && value.size() >= 2 ) {
                DailyDingInfo dailyDingInfo = value.get(0);
                DailyDingInfo dailyDingInfo2 = value.get(1);
                if ( dailyDingInfo.getErrType() == 0
                        && dailyDingInfo2.getErrType() == 0 ) {
                    //没有异常
                    Date offDutyTime = getOffDutyTime(value);
                    DateTime offBaseTime = getOffBaseTime(value);
                    if ( offDutyTime != null ) {
                        DateTime dateTime = new DateTime(offDutyTime);
                        DateTime aimTime = new DateTime(offBaseTime.getYear(), offBaseTime.getMonthOfYear(), offBaseTime.getDayOfMonth(), hour, minute, 0);
                        if ( dateTime.isAfter(aimTime.getMillis()) ) {
                            //满足条件
                            MealSupport mealSupport = new MealSupport();
                            mealSupport.setAddTime(new Date());
                            mealSupport.setDay(offBaseTime.getDayOfMonth());
                            mealSupport.setMonth(offBaseTime.getMonthOfYear());
                            mealSupport.setYear(offBaseTime.getYear());
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

    private DateTime getOffBaseTime(List<DailyDingInfo> value) {
        for ( DailyDingInfo dailyDingInfo : value ) {
            if ( dailyDingInfo.getDingType().equals("OffDuty") ) {
                return new DateTime(dailyDingInfo.getBaseTime());
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
    private Map<String, List<DailyDingInfo>> getDingInfo(String userId, String accessToken, int year, int month) {
        Map<String, List<DailyDingInfo>> dailyDingInfoMap = new HashMap<>();
        //每次只能获取到7天数据
        DateTime today = DateTime.now();
        //说明不是本月的 是其他月份
        if ( today.getYear() != year || today.getMonthOfYear() != month ) {
            today = new DateTime(year, month, 1, 12, 0, 0);
        }
        //计算最后一天
        int endDay = today.dayOfMonth().withMaximumValue().getDayOfMonth();
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
            //打卡的时间点
            DateTime thisDay = new DateTime(recordresult.getUserCheckTime());
            DailyDingInfo dingInfo = new DailyDingInfo();
            dingInfo.setDingType(recordresult.getCheckType());
            dingInfo.setDingTime(recordresult.getUserCheckTime());
            dingInfo.setErrType(StringUtils.getErrType(recordresult.getTimeResult()));
            dingInfo.setBaseTime(recordresult.getBaseCheckTime());
            dingInfo.setErrTypeDesc(recordresult.getTimeResult());
            //如果是工作日才需要操作
            DateTime baseTime = new DateTime(recordresult.getBaseCheckTime());
            DateTime todayMorning = new DateTime(baseTime.getYear(), baseTime.getMonthOfYear(), baseTime.getDayOfMonth(), 4, 0, 0);
            DateTime todayEnd = todayMorning.plusDays(1);
            String key = thisDay.getYear() + "-" + TimeUtils.formatInt(thisDay.getMonthOfYear()) + "-" + TimeUtils.formatInt(thisDay.getDayOfMonth());
            if ( thisDay.isBefore(todayEnd) && thisDay.isAfter(todayMorning) ) {
                //也算今天的打卡时间
                key = baseTime.getYear() + "-" + TimeUtils.formatInt(baseTime.getMonthOfYear()) + "-" + TimeUtils.formatInt(baseTime.getDayOfMonth());
            }
            if ( dailyDingInfoMap.containsKey(key) ) {
                List<DailyDingInfo> list = dailyDingInfoMap.get(key);
                if ( !list.contains(dingInfo) )
                    list.add(dingInfo);
            } else {
                List<DailyDingInfo> list = new ArrayList<>();
                if ( !list.contains(dingInfo) )
                    list.add(dingInfo);
                dailyDingInfoMap.put(key, list);
            }
        }
        return dailyDingInfoMap;
    }

    @Autowired
    private LogMapper logMapper;

    //跟踪记录 0 首页 1 统计详情
    private void log(int type, String user_id) {
        logMapper.insert(user_id, type);
    }

    /**
     * 检查此用户是否是特殊用户
     *
     * @return
     */
    private boolean checkUserType(String userId) {
        int userInBlack = commonMapper.isUserInBlack(userId);
        if ( userInBlack != 0 ) {
            //在黑名单
            return true;
        } else {
            return false;
        }
    }
}