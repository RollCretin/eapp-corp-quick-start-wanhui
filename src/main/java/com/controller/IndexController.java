package com.controller;

import com.config.DateBean;
import com.config.DateConfig;
import com.config.PicsConfig;
import com.config.URLConstant;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceGetusergroupRequest;
import com.dingtalk.api.request.OapiAttendanceListRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiAttendanceGetusergroupResponse;
import com.dingtalk.api.response.OapiAttendanceListResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.mapper.LogMapper;
import com.mapper.MonthTimeMapper;
import com.mapper.RemindMapper;
import com.mapper.UserConfigMapper;
import com.mapper.UserMapper;
import com.model.DataModel;
import com.model.InitInfoModel;
import com.model.UserDetailsModel;
import com.model.UserMainInfoModel;
import com.model.domain.MonthTime;
import com.model.domain.Remind;
import com.model.domain.User;
import com.model.domain.UserConfig;
import com.service.HttpClient;
import com.taobao.api.ApiException;
import com.util.AccessTokenUtil;
import com.util.ServiceResult;
import com.util.TimeUtils;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 企业内部E应用Quick-Start示例代码 实现了最简单的免密登录（免登）功能
 */
@RestController
public class IndexController {
    private static final Logger bizLogger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    public HttpClient httpClient;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserConfigMapper userConfigMapper;

    @Autowired
    private MonthTimeMapper monthTimeMapper;

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private RemindMapper remindMapper;

    /**
     * 欢迎页面,通过url访问，判断后端服务是否启动
     */
    @RequestMapping( value = "/welcome", method = RequestMethod.GET )
    public String welcome() {
        return "welcome";
    }

    /**
     * 钉钉用户登录，显示当前登录用户的userId和名称
     *
     * @param requestAuthCode 免登临时code
     */
    @RequestMapping( value = "/main", method = RequestMethod.POST )
    @ResponseBody
    public ServiceResult getMainData(@RequestParam( value = "authCode" ) String requestAuthCode, HttpServletRequest servletRequest) {
        //获取accessToken,注意正是代码要有异常流处理
        String accessToken = AccessTokenUtil.getToken();

        String userId = AccessTokenUtil.getUserId(accessToken, servletRequest, requestAuthCode);
        System.out.println("authCode：" + requestAuthCode + "  userId：" + userId);

        DateTime now = DateTime.now();

        DataModel[] dataArray = null;

        List<UserConfig> userByUserId = userConfigMapper.findUserByUserId(userId, now.getYear(), now.getMonthOfYear());

        UserMainInfoModel dakaInfo = getDakaInfo(accessToken, userId, now.getYear(), now.getMonthOfYear(), dataArray, userByUserId);

        OapiUserGetResponse userName = getUserInfo(accessToken, userId);
        String kaoqinzu = getKaoqinzu(accessToken, userId);

        dakaInfo.setUserName(userName.getName());
        if ( userName.getAvatar() == null || "".equals(userName.getAvatar()) ) {
            //头像为空 设置默认头像
            dakaInfo.setAvatar(PicsConfig.getDefaultRandomAvatar());
        } else {
            dakaInfo.setAvatar(userName.getAvatar());
        }
        dakaInfo.setGroupName(kaoqinzu);

        User userById = userMapper.findUserById(userId);
        if ( userById == null )
            userMapper.insert(userId, userName.getName(), userName.getAvatar(), kaoqinzu);

        int userType = 0;
        //固定班制  交易班制  弹性考勤
        if ( "固定班制".equals(kaoqinzu) ) {
            userType = 0;
        } else if ( "弹性考勤".equals(kaoqinzu) ) {
            userType = 1;
        } else if ( "交易班制".equals(kaoqinzu) ) {
            userType = 2;
        }
        long allTime = dakaInfo.getAllDingTime() + (dakaInfo.getIsTodayError() == 1 ? 0 : dakaInfo.getTodayTime());
        DateBean dates = DateConfig.getDates(now.getYear() + "" + (now.getMonthOfYear() < 10 ? "0" + now.getMonthOfYear() : now.getMonthOfYear()));
        //已考勤天数 包含今天
        int clockinDays = dakaInfo.getAllClockingInDays() - dakaInfo.getLeftClockingInDays();
        if ( dates.isTodayInWork() ) {
            clockinDays = clockinDays + (dakaInfo.getIsTodayError() == 1 ? 0 : 1);
        }
        //真正参与考勤的天数
        int joindDays = clockinDays - dakaInfo.getDingErrorDays();
        int status = 0;
        long dailyTime;
        if ( joindDays == 0 ) {
            dailyTime = 0;
        } else {
            dailyTime = allTime / joindDays;
        }
        if ( dailyTime >= 8 * 3600 * 1000l ) {
            //正常
            status = 0;
        } else {
            //异常
            status = 1;
        }
        dataBackup(userId, now.getYear(), now.getMonthOfYear(), userType,
                TimeUtils.formatTimeToString(allTime), TimeUtils.formatTimeToString(dailyTime),
                dakaInfo.getDingErrorDays(),
                status, clockinDays, dakaInfo.getMonthErrDays());
        ServiceResult serviceResult = ServiceResult.success(dakaInfo);

        return serviceResult;
    }

    @RequestMapping( value = "/details", method = RequestMethod.POST )
    @ResponseBody
    public ServiceResult getMainDetails(@RequestParam( value = "authCode" ) String requestAuthCode,
                                        @RequestParam( value = "time" ) String time, HttpServletRequest servletRequest) {
        String accessToken = AccessTokenUtil.getToken();

        String userId = AccessTokenUtil.getUserId(accessToken, servletRequest, requestAuthCode);

        log(1, userId);

        int year = Integer.parseInt(time.split("-")[0]);
        int month = Integer.parseInt(time.split("-")[1]);

        DataModel[] dataArray = null;

        List<UserConfig> userByUserId = userConfigMapper.findUserByUserId(userId, year, month);

        AtomicReference<UserDetailsModel> dakaDetails = new AtomicReference<>(getDakaDetails(accessToken, userId, year, month, dataArray, userByUserId));

        ServiceResult serviceResult = ServiceResult.success(dakaDetails.get());
        return serviceResult;
    }

    @RequestMapping( value = "/remind", method = RequestMethod.POST )
    @ResponseBody
    public ServiceResult setRemind(@RequestParam( value = "authCode" ) String requestAuthCode, @RequestParam( value = "status" ) int status,
                                   HttpServletRequest servletRequest) {
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, servletRequest, requestAuthCode);

        Remind remindByUserId = remindMapper.findRemindByUserId(userId);
        if ( status == 0 ) {
            //关闭
            if ( remindByUserId != null ) {
                int row = remindMapper.update(remindByUserId.getId(), status);
                if ( row == 0 ) {
                    ServiceResult serviceResult = ServiceResult.failure();
                    return serviceResult;
                }
            }
        } else {
            //打开
            if ( remindByUserId != null ) {
                int row = remindMapper.update(remindByUserId.getId(), status);
                if ( row == 0 ) {
                    ServiceResult serviceResult = ServiceResult.failure();
                    return serviceResult;
                }
            } else {
                //之前没有数据 新增
                int row = remindMapper.insert(userId, status);
                if ( row == 0 ) {
                    ServiceResult serviceResult = ServiceResult.failure();
                    return serviceResult;
                }
            }
        }
        ServiceResult serviceResult = ServiceResult.success("设置成功");
        return serviceResult;
    }

    @RequestMapping( value = "/set_day", method = RequestMethod.POST )
    @ResponseBody
    public ServiceResult setDayInfo(@RequestParam( value = "authCode" ) String requestAuthCode,
                                    @RequestParam( value = "time" ) String time,
                                    @RequestParam( value = "type" ) int type, HttpServletRequest servletRequest) {
        //标记类型 1 请假 2 外勤 3 出差
        String accessToken = AccessTokenUtil.getToken();

        String userId = AccessTokenUtil.getUserId(accessToken, servletRequest, requestAuthCode);

        int year = Integer.parseInt(time.split("-")[0]);
        int month = Integer.parseInt(time.split("-")[1]);
        int day = Integer.parseInt(time.split("-")[2]);

        List<UserConfig> user = userConfigMapper.findUser(userId, year, month, day);
        if ( user == null || user.isEmpty() ) {
            //没有数据 插入数据
            int row = userConfigMapper.insertData(userId, year, month, day, type);
            if ( row != 0 ) {
            } else {
                //
                ServiceResult serviceResult = ServiceResult.failure();
                return serviceResult;
            }
        } else {
            int row = userConfigMapper.updateUserConfigTypeById(user.get(0).getId(), type);
            if ( row != 0 ) {
            } else {
                //
                ServiceResult serviceResult = ServiceResult.failure();
                return serviceResult;
            }
        }
        List<UserConfig> userByUserId = userConfigMapper.findUserByUserId(userId, year, month);
        DataModel[] dataArray = null;

        UserDetailsModel dakaDetails = getDakaDetails(accessToken, userId, year, month, dataArray, userByUserId);

        ServiceResult serviceResult = ServiceResult.success(dakaDetails);
        return serviceResult;
    }


    /**
     * 获取用户姓名
     *
     * @param accessToken
     * @param userId
     * @return
     */
    private OapiUserGetResponse getUserInfo(String accessToken, String userId) {
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
     * 获取考勤组名字
     *
     * @param accessToken
     * @param userId
     * @return
     */
    private String getKaoqinzu(String accessToken, String userId) {
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

    private long NUM = 50;

    //获取信息
    private void getData(int year, int month, int lastDay, int start, long offset, String userId, String accessToken, DingTalkClient client, DataModel[] dataArray) {
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
        request.setLimit(NUM);
        try {
            OapiAttendanceListResponse response = client.execute(request, accessToken);
            List<OapiAttendanceListResponse.Recordresult> recordresult1 = response.getRecordresult();
            if ( recordresult1 != null ) {
                offset += recordresult1.size();
                if ( recordresult1.size() < NUM ) {
                    //数据已结束
                    analyse(recordresult1, dataArray);
                } else {
                    //数据没有结束
                    analyse(recordresult1, dataArray);
                    getData(year, month, lastDay, start, offset, userId, accessToken, client, dataArray);
                }
            }
        } catch ( ApiException e ) {
            e.printStackTrace();
        }
    }

    //分析数据
    private void analyse(List<OapiAttendanceListResponse.Recordresult> recordresult1, DataModel[] dataArray) {
        //将数据按天数添加到数组中
        for ( OapiAttendanceListResponse.Recordresult recordresult : recordresult1 ) {
            for ( DataModel initDatum : dataArray ) {
                long userTime = recordresult.getUserCheckTime() == null ? 0 : recordresult.getUserCheckTime().getTime();
                if ( userTime < initDatum.getEndTime() &&
                        userTime > initDatum.getStartTime() ) {
                    List<DataModel.DingModel> list = initDatum.getList();
                    if ( list == null ) {
                        list = new ArrayList<>();
                    }
                    DataModel.DingModel dingModel = new DataModel.DingModel();
                    dingModel.setTime(userTime);
                    dingModel.setType(recordresult.getCheckType());
                    if ( !list.contains(dingModel) ) {
                        list.add(dingModel);
                    }
                    initDatum.setList(list);
                }
            }
        }
    }

    //获取打卡信息
    private UserMainInfoModel getDakaInfo(String accessToken, String userId, int year, int month, DataModel[] dataArray, List<UserConfig> userByUserId) {
        UserMainInfoModel userMainInfoModel = new UserMainInfoModel();
        DateTime firstDate = new DateTime(year, month, 1, 0, 0, 0);
        DateTime today = new DateTime();
        int lastDay = firstDate.dayOfMonth().withMaximumValue().dayOfMonth().get();

        //获取是否开启自动提醒功能
        Remind remindByUserId = remindMapper.findRemindByUserId(userId);
        if ( remindByUserId != null ) {
            userMainInfoModel.setOpenRemind(remindByUserId.getStatus() == 0 ? false : true);
        }
        dataArray = new DataModel[lastDay];

        //初始化数据
        InitInfoModel initInfoModel = initInfo(year, month, lastDay, userMainInfoModel, userId, accessToken, today, dataArray, userByUserId);
        long wholeMothTime = initInfoModel.getAllTime();
        userMainInfoModel.setMonthErrDays(initInfoModel.getErrDays());

        //计算总时长 这个是不包含异常天数数据的
        userMainInfoModel.setAllDingTime(wholeMothTime);
        userMainInfoModel.setAllDingTimeDesc(TimeUtils.formatTimeToString(wholeMothTime));

        DateBean dates = DateConfig.getDates(year + "" + (month < 10 ? "0" + month : month));

        //截止昨天上了多少天班
        int workDays = dates.getDaysBefore(today.getDayOfMonth());

        int errorDays = 0;
        //今日打卡是否异常
        boolean todayError = false;
        for ( UserConfig userConfig : userByUserId ) {
            List<DateBean.MonthBean> month1 = dates.getMonth();
            HH:
            for ( DateBean.MonthBean monthBean : month1 ) {
                if ( Integer.parseInt(monthBean.getDay()) == userConfig.getDay() ) {
                    errorDays++;
                    if ( userConfig.getDay() == today.getDayOfMonth() ) {
                        todayError = true;
                    }
                    break HH;
                }
            }
        }
        //设置打卡异常天数
        userMainInfoModel.setDingErrorDays(errorDays);
        //设置今日是否打卡异常
        userMainInfoModel.setIsTodayError(todayError ? 1 : 0);

        //所有考勤天数
        int allClockingInDays = dates.getMonth().size();
        //本月考勤天数
        userMainInfoModel.setAllClockingInDays(allClockingInDays);
        //本月剩余考勤天数
//        userMainInfoModel.setLeftClockingInDays(allClockingInDays - workDays - errorDays + (todayError ? 1 : 0));

        //截止今天 需要的总时长 分钟
        long needTime = (workDays - errorDays + (todayError ? 1 : 0)) * 8 * 60 * 60 * 1000l;
        long tempTime = wholeMothTime - needTime;
        userMainInfoModel.setMistimingTime(tempTime);
        userMainInfoModel.setMistimingTimeDesc(TimeUtils.formatTimeToString(Math.abs(tempTime)));

        //平均每日上班时长
        long everyDayTime = 0l;
        int avgWorkDays = workDays - errorDays + (todayError ? 1 : 0);
        if ( avgWorkDays <= 0 ) {
            everyDayTime = 0l;
        } else {
            everyDayTime = wholeMothTime / avgWorkDays;
        }
        userMainInfoModel.setDailyDingTime(everyDayTime);
        userMainInfoModel.setDailyDingTimeDesc(TimeUtils.formatTimeToString(everyDayTime));

        //更正需要加入统计的天数
        workDays = workDays - errorDays + (todayError ? 1 : 0);

        long allDingTime = (allClockingInDays - errorDays) * 8 * 60 * 60 * 1000l;
        //剩余考勤时长
        userMainInfoModel.setLeftDingTime(allDingTime - wholeMothTime);
        userMainInfoModel.setLeftDingTimeDesc(TimeUtils.formatTimeToString(allDingTime - wholeMothTime));
        //剩余每天打卡平均时长
        if ( allClockingInDays - workDays - errorDays == 0 ) {
            userMainInfoModel.setLeftDailyDingTime(0);
            userMainInfoModel.setLeftDailyDingTimeDesc(TimeUtils.formatTimeToString(0l));
        } else {
            long leftDailyDingTime = (allDingTime - wholeMothTime) / (allClockingInDays - workDays - errorDays);
            userMainInfoModel.setLeftDailyDingTime(leftDailyDingTime);
            userMainInfoModel.setLeftDailyDingTimeDesc(TimeUtils.formatTimeToString(leftDailyDingTime));
        }
        //本月剩余考勤天数
        userMainInfoModel.setLeftClockingInDays(allClockingInDays - workDays - errorDays);

        return userMainInfoModel;
    }

    //获取打卡详情
    private UserDetailsModel getDakaDetails(String accessToken, String userId, int year, int month, DataModel[] dataArray, List<UserConfig> userConfigs) {
        UserDetailsModel userDetailsModel = new UserDetailsModel();
        userDetailsModel.setMonth(year + "-" + month);
        List<UserDetailsModel.DetailsModel> result = new ArrayList<>();
        DateTime dateTime = new DateTime(year, month, 1, 0, 0, 0);
        DateTime today = DateTime.now();
        int lastDay = dateTime.dayOfMonth().withMaximumValue().dayOfMonth().get();

        dataArray = new DataModel[lastDay];

        //初始化数据
        InitInfoModel initInfoModel = initInfo(year, month, lastDay, null, userId, accessToken, today, dataArray, userConfigs);
        long wholeMothTime = initInfoModel.getAllTime();

        //计算总时长
        userDetailsModel.setAllDingTime(wholeMothTime);
        userDetailsModel.setAllDingTimeDesc(TimeUtils.formatTimeToString(wholeMothTime));

        DateBean dates = DateConfig.getDates(year + "" + (month < 10 ? "0" + month : month));
        //考勤总天数
        int allClockingInDays = dates.getMonth().size();

        //截止昨天上了多少天班
        int workDays = 0;
        if ( today.getYear() == year && today.getMonthOfYear() == month ) {
            //本月
            workDays = dates.getDays(today.getDayOfMonth());
        } else {
            //不是本月
            workDays = allClockingInDays;
        }

        //设置考勤总天数
        userDetailsModel.setAllClockingInDays(allClockingInDays);

        int errorDay = 0;

        for ( UserConfig userConfig : userConfigs ) {
            List<DateBean.MonthBean> month11 = dates.getMonth();
            HH:
            for ( DateBean.MonthBean monthBean : month11 ) {
                if ( Integer.parseInt(monthBean.getDay()) == userConfig.getDay() ) {
                    errorDay++;
                    break HH;
                }
            }
        }
        //设置打卡异常天数
        userDetailsModel.setDingErrorDays(errorDay);
        //已参与考勤的天数
        userDetailsModel.setHasJoinedDays(workDays - errorDay);

        //平均每日上班时长
        int avgWorkDays = workDays - errorDay;
        long everyDayTime = 0;
        if ( avgWorkDays == 0 ) {
            everyDayTime = 0;
        } else {
            everyDayTime = wholeMothTime / avgWorkDays;
        }
        userDetailsModel.setDailyDingTime(everyDayTime);
        userDetailsModel.setDailyDingTimeDesc(TimeUtils.formatTimeToString(everyDayTime));

        //截止今天 需要的总时长 分钟
        long needTime = (allClockingInDays - errorDay) * 8 * 60 * 60 * 1000l;
        long tempTime = wholeMothTime - needTime;
        userDetailsModel.setMistimingTime(tempTime);
        userDetailsModel.setMistimingTimeDesc(TimeUtils.formatTimeToString(Math.abs(tempTime)));

        //处理数据
        for ( DataModel dataModel : dataArray ) {
            if ( dataModel.getList() != null && !dataModel.getList().isEmpty() ) {
                UserDetailsModel.DetailsModel detailsModel = new UserDetailsModel.DetailsModel();
                detailsModel.setDingDate(dataModel.getAimTime().split(" ")[0]);
                detailsModel.setDingTime(dataModel.getMills());
                detailsModel.setDingTimeDesc(TimeUtils.formatTimeToString(dataModel.getMills()));
                int checkType = checkType(userConfigs, dataModel.getStartTime(), userId);
                detailsModel.setDingType(checkType);
                if ( dataModel.getMills() <= 0 ) {
                    detailsModel.setStatus(0);
                } else {
                    detailsModel.setStatus(1);
                }
                detailsModel.setTodayDing(dataModel.getSteps());
                result.add(detailsModel);
            }
        }
        Collections.reverse(result);
        userDetailsModel.setList(result);
        return userDetailsModel;
    }

    /**
     * 初始化数据
     *
     * @param year
     * @param month
     * @param lastDay
     * @param userMainInfoModel
     * @param userId
     * @param accessToken
     * @param today
     * @param userConfigs
     */
    private InitInfoModel initInfo(int year, int month, int lastDay, UserMainInfoModel userMainInfoModel, String userId, String accessToken, DateTime today, DataModel[] dataArray, List<UserConfig> userConfigs) {
        InitInfoModel initInfoModel = new InitInfoModel();
        StringBuilder errDays = new StringBuilder();
        long wholeMothTime = 0;
        //遍历给数组添加数据
        for ( int i = 0; i < dataArray.length; i++ ) {
            DataModel dataModel = new DataModel();
            DateTime time = new DateTime(year, month, (i + 1), 4, 0, 0);
            dataModel.setAimTime(time.toString("yyyy-MM-dd HH:mm:ss"));
            dataModel.setStartTime(time.getMillis());
            dataModel.setEndTime(time.getMillis() + 24000 * 3600);
            dataModel.setTimeLunchStart(time.getMillis() + 8000 * 3600);
            dataModel.setTimeLunchEnd(time.getMillis() + 9500 * 3600);
            dataModel.setTimeMorningStart(time.getMillis() + 5000 * 3600);
            dataArray[i] = dataModel;
        }

        //每次只能获取到7天数据
        int times = lastDay / 7 + (lastDay % 7 == 0 ? 0 : 1);
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/list");
        for ( int i = 0; i < times; i++ ) {
            getData(year, month, lastDay, i, 0, userId, accessToken, client, dataArray);
        }

        DateBean dates = DateConfig.getDates(year + "" + (month < 10 ? "0" + month : month));

        //对数据进行统计
        for ( DataModel dataModel : dataArray ) {
            List<DataModel.DingModel> list = dataModel.getList();
            //判断需不需要进行请假等日期的判断
            boolean ignore = false;
            int checkType = checkType(userConfigs, dataModel.getStartTime(), userId);
            if ( checkType != 0 ) {
                ignore = true;
            }
            if ( list != null && !list.isEmpty() ) {
                //先排序
                Collections.sort(list);
                boolean start = true;
                long allTime = 0;
                long startTime = 0;
                List<UserMainInfoModel.DingModel> steps = new ArrayList<>();
                for ( DataModel.DingModel dingModel : list ) {
                    //对真实时间做下处理
                    long userTime = dingModel.getTime();
                    long meetTimeStart = dingModel.getTime();
                    if ( userTime < dataModel.getTimeMorningStart() ) {
                        userTime = dataModel.getTimeMorningStart();
                        meetTimeStart = dataModel.getTimeMorningStart() + 1500 * 3600l;
                    } else if ( userTime > dataModel.getTimeLunchStart() && userTime < dataModel.getTimeLunchEnd() ) {
                        //如果是中午打卡
                        userTime = dataModel.getTimeLunchStart();
                        meetTimeStart = dataModel.getTimeLunchEnd();
                    } else if ( userTime > dataModel.getTimeLunchEnd() ) {
                        //1：30以后打卡
                        userTime = userTime - 1500 * 3600l;
                        //照旧
                    } else if ( userTime < dataModel.getTimeLunchEnd() ) {
                        //09:00~12:00之间打卡
                        //照旧
                        meetTimeStart = meetTimeStart + 1500 * 3600l;
                    }

                    //OnDuty OffDuty
                    if ( "OnDuty".equals(dingModel.getType()) ) {
                        start = true;
                        startTime = userTime;

                        if ( userMainInfoModel != null && today.toDateMidnight().isEqual(new DateMidnight(dataModel.getStartTime())) ) {
                            if ( steps.size() == 0 ) {
                                //第一次上班打卡
                                long meetTodayTime = meetTimeStart + 8 * 3600 * 1000l;
                                userMainInfoModel.setMeetTodayTtime(meetTodayTime);
                                userMainInfoModel.setMeetTodayTtimeDesc(new DateTime(meetTodayTime).toString("yyyy/MM/dd HH:mm:ss"));
                            }
                        }

                        steps.add(new UserMainInfoModel.DingModel(dingModel.getTime(), 0,
                                new DateTime(dingModel.getTime()).toString("yyyy/MM/dd HH:mm:ss" + " 上班打卡")));

                    }
                    if ( "OffDuty".equals(dingModel.getType()) ) {
                        if ( start ) {
                            //有效的
                            allTime = allTime + userTime - startTime;
                        }
                        steps.add(new UserMainInfoModel.DingModel(dingModel.getTime(), 1,
                                new DateTime(dingModel.getTime()).toString("yyyy/MM/dd HH:mm:ss" + " 下班打卡")));
                        start = false;
                    }
                }
                //判断是不是今天
                if ( userMainInfoModel != null && today.toDateMidnight().isEqual(new DateMidnight(dataModel.getStartTime())) ) {
                    userMainInfoModel.setTodayDing(steps);
                    userMainInfoModel.setTodayTime(allTime);
                    userMainInfoModel.setTodayTimeDesc(TimeUtils.formatTimeToString(allTime));

                    //满足今天下班打卡时间
                    if ( steps == null || steps.isEmpty() ) {
                        userMainInfoModel.setMeetTodayTtime(0);
                        userMainInfoModel.setMeetTodayTtimeDesc(TimeUtils.formatTimeToString(0l));
                    }
                } else {
                    if ( !ignore )
                        wholeMothTime = wholeMothTime + allTime;
                }
                dataModel.setSteps(steps);
                dataModel.setMills(allTime);
                if ( allTime == 0 ) {
                    errDays.append(new DateTime(dataModel.getStartTime()).toString("yyyy/MM/dd") + ",");
                }
            } else {
                //今日没有打卡信息
                //判断当日否是工作日以及今天以及今天以前的日期 是的话需要添加一些默认数据
                DateTime aim = new DateTime(dataModel.getStartTime());
                if ( dates.isInWork(aim.getDayOfMonth()) ) {
                    DateTime t = new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 4, 0, 0);
                    //时间大于指定时间返回 1 时间小于指定时间返回-1 相等返回0
                    if ( t.compareTo(aim) != -1 ) {
                        List<DataModel.DingModel> lists = new ArrayList<>();
                        DataModel.DingModel dingModel = new DataModel.DingModel();
                        dingModel.setTime(0);
                        dingModel.setType("OnDuty");
                        DataModel.DingModel dingModel1 = new DataModel.DingModel();
                        dingModel1.setTime(0);
                        dingModel1.setType("OffDuty");
                        lists.add(dingModel);
                        lists.add(dingModel1);
                        dataModel.setList(lists);

                        List<UserMainInfoModel.DingModel> steps = new ArrayList<>();
                        steps.add(new UserMainInfoModel.DingModel(0, 0,
                                "今日无上班打卡信息"));
                        steps.add(new UserMainInfoModel.DingModel(0, 1,
                                "今日无下班打卡信息"));
                        dataModel.setSteps(steps);
                        dataModel.setMills(0);
                        errDays.append(aim.toString("yyyy/MM/dd") + ",");
                    }
                }
            }
        }
        initInfoModel.setAllTime(wholeMothTime);
        if ( errDays.toString() != null && !errDays.toString().equals("") && !errDays.toString().equals("0,") ) {
            initInfoModel.setErrDays(errDays.subSequence(0, errDays.length() - 1).toString());
        }
        return initInfoModel;
    }

    /**
     * 检查是否包含指定天数 有返回对应类型 否则返回0
     *
     * @param userConfigs
     * @param time
     * @param userId
     * @return
     */
    private int checkType(List<UserConfig> userConfigs, long time, String userId) {
        if ( userConfigs != null && !userConfigs.isEmpty() ) {
            UserConfig userConfig = new UserConfig();
            DateTime dateTime = new DateTime(time);
            userConfig.setUserId(userId);
            userConfig.setDay(dateTime.getDayOfMonth());
            userConfig.setYear(dateTime.getYear());
            userConfig.setMonth(dateTime.getMonthOfYear());
            if ( userConfigs.contains(userConfig) ) {
                return userConfigs.get(userConfigs.indexOf(userConfig)).getType();
            }
        }
        return 0;
    }

    //跟踪记录 0 首页 1 统计详情
    private void log(int type, String user_id) {
        logMapper.insert(user_id, type);
    }

    @Async( "processExecutor" )
    public void dataBackup(String userId, int year, int month, int user_type, String all_time, String daily_time, int err_day, int status, int clockin_day, String monthErrDays) {
        //log记录
        log(0, userId);

        //统计用户打卡信息
        MonthTime monthTimeComfig = monthTimeMapper.findMonthTimeComfig(userId, year, month, user_type);
        if ( monthTimeComfig == null ) {
            //之前没有数据
            monthTimeMapper.insert(userId, month, year, all_time, daily_time, err_day, user_type, status, clockin_day, monthErrDays);
        } else {
            //之前有 更新
            monthTimeMapper.update(monthTimeComfig.getId(), userId, month, year, all_time, daily_time, err_day, user_type, status, clockin_day, monthErrDays);
        }
    }
}


