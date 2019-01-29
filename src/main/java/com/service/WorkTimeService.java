/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: WorkTimeService
 * Author:   cretin
 * Date:     1/28/19 19:00
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.service;

import com.config.Constant;
import com.config.DateBean;
import com.config.DateConfig;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceListRequest;
import com.dingtalk.api.response.OapiAttendanceListResponse;
import com.mapper.CommonMapper;
import com.model.DailyDingInfo;
import com.model.domain.User;
import com.model.domain.UserManager;
import com.model.excel.ExcelData;
import com.model.response.UserAimWorkTimeResp;
import com.model.response.UserWorkTimeResp;
import com.taobao.api.ApiException;
import com.util.ExcelUtils;
import com.util.StringUtils;
import com.util.TimeUtils;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.config.Constant.COMMON_PAGE_NUM;

/**
 * 〈〉
 *
 * @author cretin
 * @create 1/28/19
 * @since 1.0.0
 */
@Service
public class WorkTimeService {
    @Autowired
    private MailService mailService;

    @Autowired
    private CommonMapper commonMapper;

    /**
     * 获取指定人的信息
     *
     * @param accessToken
     * @param year
     * @param month
     * @param user
     * @return
     */
    public UserAimWorkTimeResp getAimData(String accessToken, int year, int month, User user) {
        UserAimWorkTimeResp userAimWorkTimeResp = new UserAimWorkTimeResp();
        long allTime = 0;
        Map<String, List<DailyDingInfo>> dingInfo = getDingInfo(user.getId(), accessToken, year, month);
        List<UserAimWorkTimeResp.DingInfo> dingInfos = new ArrayList<>();
        for ( Map.Entry<String, List<DailyDingInfo>> entry : dingInfo.entrySet() ) {
            List<DailyDingInfo> value = entry.getValue();
            if ( checkData(value) ) {
                //检查时间
                long workTime = getWorkTime(value);
                allTime += workTime;
                UserAimWorkTimeResp.DingInfo dingInfo1 = getDingInfo(value);
                dingInfo1.setTodayTime(TimeUtils.formatTimeToString(workTime));
                dingInfos.add(dingInfo1);
            }
        }
        userAimWorkTimeResp.setUserId(user.getId());
        userAimWorkTimeResp.setUserName(user.getUserName());
        userAimWorkTimeResp.setAllTime(TimeUtils.formatTimeToString(allTime));
        Collections.sort(dingInfos, new Comparator<UserAimWorkTimeResp.DingInfo>() {
            @Override
            public int compare(UserAimWorkTimeResp.DingInfo o1, UserAimWorkTimeResp.DingInfo o2) {
                return ( int ) (o1.getDate().getTime() - o2.getDate().getTime());
            }
        });
        userAimWorkTimeResp.setList(dingInfos);
        return userAimWorkTimeResp;
    }

    private UserAimWorkTimeResp.DingInfo getDingInfo(List<DailyDingInfo> value) {
        UserAimWorkTimeResp.DingInfo dingInfo = new UserAimWorkTimeResp.DingInfo();
        DailyDingInfo onDuty = getTypeDing(value, "OnDuty");
        DailyDingInfo offDuty = getTypeDing(value, "OffDuty");
        StringBuilder stringBuilder = new StringBuilder();
        if ( onDuty != null ) {
            dingInfo.setOnTime(new DateTime(onDuty.getDingTime()).toString(Constant.DATE_FORMAT));
            stringBuilder.append("上班:" + StringUtils.getChineseErrType(onDuty.getErrType()) + " ");
            dingInfo.setDate(onDuty.getBaseTime());
        } else {
            dingInfo.setOnTime("上班未打卡");
        }
        if ( offDuty != null ) {
            dingInfo.setOffTime(new DateTime(offDuty.getDingTime()).toString(Constant.DATE_FORMAT));
            stringBuilder.append("下班:" + StringUtils.getChineseErrType(offDuty.getErrType()) + " ");
        } else {
            dingInfo.setOnTime("下班未打卡");
        }
        dingInfo.setStateDesc(stringBuilder.toString());
        return dingInfo;
    }

    @Async( "processExecutor" )
    public void dataBackup(String accessToken, int year, int month, List<User> users, String emails) {
        List<UserWorkTimeResp> resps = new ArrayList<>();
        int index = 0;
        for ( User user : users ) {
            long allTime = 0;
            Map<String, List<DailyDingInfo>> dingInfo = getDingInfo(user.getId(), accessToken, year, month);
            for ( Map.Entry<String, List<DailyDingInfo>> entry : dingInfo.entrySet() ) {
                List<DailyDingInfo> value = entry.getValue();
                if ( checkData(value) ) {
                    //检查时间
                    allTime += getWorkTime(value);
                }
            }
            UserWorkTimeResp userWorkTimeResp = new UserWorkTimeResp();
            userWorkTimeResp.setUserId(user.getId());
            userWorkTimeResp.setUserName(user.getUserName());
            userWorkTimeResp.setAllWorkTime(TimeUtils.formatTimeToString(allTime));
            userWorkTimeResp.setGroupName(user.getGroupName());
            System.out.println("第" + index + "个：" + userWorkTimeResp.toString());
            index++;
            resps.add(userWorkTimeResp);
        }
        createExcel(resps, emails);
    }

    private void createExcel(List<UserWorkTimeResp> resps, String emails) {
        DateTime now = DateTime.now();
        String fileName = "followme_worktime_" + DateTime.now().toString("yyyyMMddHHmmsss") + ".xlsx";
        File file = new File(Constant.EXCEL_PATH);
        if ( !file.exists() && !file.isDirectory() ) {
            file.mkdirs();
        }
        File aimFile = new File(file, fileName);
        List<UserManager> allUserManager = new ArrayList<>();
        if ( StringUtils.isEmpty(emails) ) {
            allUserManager = commonMapper.getAllUserManager();
        } else {
            UserManager userMapper = new UserManager();
            userMapper.setUserName("管理员");
            userMapper.setEmail(emails);
            allUserManager.add(userMapper);
        }
        if ( allUserManager == null || allUserManager.isEmpty() )
            return;
        if ( !aimFile.exists() ) {
            //获取配置
            ExcelData data = new ExcelData();
            data.setName(now.getYear() + "年" + now.getMonthOfYear() + "月员工工作时长表");
            List<String> titles = new ArrayList();
            titles.add("序号");
            titles.add("员工ID");
            titles.add("员工姓名");
            titles.add("考情组");
            titles.add("上班总时长");
            titles.add("备注");
            data.setTitles(titles);

            List<List<Object>> rows = new ArrayList();
            int index = 1;
            for ( UserWorkTimeResp resp : resps ) {
                List<Object> row = new ArrayList();
                row.add(index);
                row.add(resp.getUserId());
                row.add(resp.getUserName());
                row.add(resp.getGroupName());
                row.add(resp.getAllWorkTime());
                row.add("");
                rows.add(row);
                index++;
            }

            data.setRows(rows);

            //生成本地
            try {
                FileOutputStream out = new FileOutputStream(aimFile);
                ExcelUtils.exportExcel(data, out);
                out.close();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }

        for ( UserManager userManager : allUserManager ) {
            sendEmail(mailService, userManager, now, fileName, 5);
        }
    }

    /**
     * 发送邮件
     *
     * @param userManager
     * @param now
     * @param fileName
     * @param times
     */
    public static void sendEmail(MailService mailService, UserManager userManager, DateTime now, String fileName, int times) {
        try {
            //mico@followme.cn  792075058@qq.com
            mailService.sendMail(userManager.getEmail(),
                    "万汇互联" + now.toString("yyyy年MM月") + "员工工作时长表",
                    "尊敬的管理员：" + userManager.getUserName() + ":\n你好！\n此邮件由《FM小助手》小程序自动生成，附件为"
                            + "万汇互联" + now.toString("yyyy年MM月") + "员工工作时长表统计Excel表，请查收。如遇文件打不开请联系我再次获取！\n"
                            + DateTime.now().toString("yyyy-MM-dd HH:mm:ss" + "\n"),
                    Constant.EXCEL_PATH + File.separatorChar + fileName);
        } catch ( Exception e ) {
            times--;
            if ( times > 0 ) {
                sendEmail(mailService, userManager, now, fileName, times);
            }
            e.printStackTrace();
        }
    }

    /**
     * 获取上班时长
     *
     * @param value
     */
    private long getWorkTime(List<DailyDingInfo> value) {
        //获取上班打卡
        DailyDingInfo onDuty = getTypeDing(value, "OnDuty");
        DailyDingInfo offDuty = getTypeDing(value, "OffDuty");
        if ( onDuty == null || offDuty == null )
            return 0;
        //对上班打卡进行校验
        DateTime baseTime = new DateTime(onDuty.getBaseTime());
        DateTime onDutyTime = new DateTime(onDuty.getDingTime());
        DateTime time12 = new DateTime(baseTime.getYear(), baseTime.getMonthOfYear(), baseTime.getDayOfMonth(), 12, 0, 0);
        DateTime time135 = new DateTime(baseTime.getYear(), baseTime.getMonthOfYear(), baseTime.getDayOfMonth(), 13, 30, 0);
        DateTime offDutyTime = new DateTime(offDuty.getDingTime());
        if ( onDutyTime.isAfter(time12) ) {
            //12点后上班
            if ( onDutyTime.isAfter(time135) ) {
                //12点后上班 13:30后上班
                if ( offDutyTime.isAfter(time135) ) {
                    //12点后上班 13:30后上班 13:30后下班
                    return offDutyTime.getMillis() - onDutyTime.getMillis();
                } else {
                    //12点后上班 13:30后上班 13:30前下班
                    return 0;
                }
            } else {
                //12点后上班 13:30前上班
                if ( offDutyTime.isAfter(time135) ) {
                    //12点后上班 13:30前上班 13:30后下班
                    return offDutyTime.getMillis() - time135.getMillis();
                } else {
                    //12点后上班 13:30前上班 13:30前下班
                    return 0;
                }
            }
        } else {
            //12点前上班
            //12点前上班 13:30前上班
            if ( offDutyTime.isAfter(time135) ) {
                //12点前上班 13:30后下班
                return offDutyTime.getMillis() - onDutyTime.getMillis() - 1500 * 3600;
            } else {
                //12点前上班 13:30前上班 13:30前下班
                return time12.getMillis() - onDutyTime.getMillis();
            }
        }
    }

    //OnDuty：上班
    //OffDuty：下班
    private boolean checkData(List<DailyDingInfo> value) {
        if ( value != null && value.size() >= 2 ) {
            //命中次数
            int aim = 0;
            for ( DailyDingInfo dailyDingInfo : value ) {
                if ( dailyDingInfo.getDingType().equals("OnDuty") ) {
                    aim++;
                }
                if ( dailyDingInfo.getDingType().equals("OffDuty") ) {
                    aim++;
                }
            }
            if ( aim == 2 ) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取指定类型的打卡数据
     *
     * @param value
     * @param type
     * @return
     */
    private DailyDingInfo getTypeDing(List<DailyDingInfo> value, String type) {
        for ( DailyDingInfo dailyDingInfo : value ) {
            if ( dailyDingInfo.getDingType().equals(type) ) {
                return dailyDingInfo;
            }
        }
        return null;
    }

    //获取本月打卡信息
    private Map<String, List<DailyDingInfo>> getDingInfo(String userId, String accessToken, int year, int month) {
        Map<String, List<DailyDingInfo>> dailyDingInfoMap = new HashMap<>();
        DateBean dates = DateConfig.getDates(year + "" + (month < 10 ? "0" + month : month));
        //每次只能获取到7天数据
        DateTime today = DateTime.now();
        //说明不是本月的 是其他月份
        if ( today.getYear() != year && today.getMonthOfYear() != month ) {
            today = new DateTime(year, month, 1, 12, 0, 0);
        }
        //计算最后一天
        int endDay = today.dayOfMonth().withMaximumValue().getDayOfMonth();
        int times = endDay / 7 + (endDay % 7 == 0 ? 0 : 1);
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/list");
        for ( int i = 0; i < times; i++ ) {
            dailyDingInfoMap = getDingInfoDetail(today.getYear(), today.getMonthOfYear(), endDay, i, 0, userId, accessToken, client, dailyDingInfoMap, dates);
        }
        return dailyDingInfoMap;
    }

    //获取信息
    private Map<String, List<DailyDingInfo>> getDingInfoDetail(int year, int month, int lastDay, int start, long offset, String userId, String accessToken, DingTalkClient client, Map<String, List<DailyDingInfo>> daikyList, DateBean dates) {
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
                    daikyListRes = analyse(recordresult, daikyListRes, dates);
                } else {
                    //数据没有结束
                    daikyListRes = analyse(recordresult, daikyListRes, dates);
                    daikyListRes = getDingInfoDetail(year, month, lastDay, start, offset, userId, accessToken, client, daikyListRes, dates);
                }
            }
        } catch ( ApiException e ) {
            e.printStackTrace();
        }
        return daikyListRes;
    }

    private Map<String, List<DailyDingInfo>> analyse(List<OapiAttendanceListResponse.Recordresult> recordresultList, Map<String, List<DailyDingInfo>> dailyDingInfoMap, DateBean dates) {
        //将数据按天数添加到数组中
        HH:
        for ( OapiAttendanceListResponse.Recordresult recordresult : recordresultList ) {
            //如果是工作日才需要操作
            DateTime baseTime = new DateTime(recordresult.getBaseCheckTime());
            if ( !dates.isInWork(baseTime.getDayOfMonth()) ) {
                continue HH;
            }

            //打卡的时间点
            DateTime thisDay = new DateTime(recordresult.getUserCheckTime());
            DailyDingInfo dingInfo = new DailyDingInfo();
            dingInfo.setDingType(recordresult.getCheckType());
            dingInfo.setDingTime(recordresult.getUserCheckTime());
            dingInfo.setErrType(StringUtils.getErrType(recordresult.getTimeResult()));
            dingInfo.setBaseTime(recordresult.getBaseCheckTime());
            dingInfo.setErrTypeDesc(recordresult.getTimeResult());

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
}