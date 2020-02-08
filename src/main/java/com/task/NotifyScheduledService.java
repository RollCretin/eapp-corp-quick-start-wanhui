package com.task;

import com.config.DateBean;
import com.config.DateConfig;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.mapper.MonthTimeMapper;
import com.mapper.RemindMapper;
import com.mapper.UserMapper;
import com.model.domain.MonthTime;
import com.model.domain.Remind;
import com.taobao.api.ApiException;
import com.util.AccessTokenUtil;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 日常通知定时任务
 */
@Component
public class NotifyScheduledService {
    @Autowired
    private RemindMapper remindMapper;

    @Autowired
    private MonthTimeMapper monthTimeMapper;

    @Autowired
    private UserMapper userMapper;

    //针对晚上18：30下班的人
//    @Scheduled( cron = "0 30 18 * * *" )
//    public void scheduled() {
//        //在这里发送消息
//        List<Remind> allReminds = remindMapper.findAllReminds();
//        if ( allReminds == null || allReminds.isEmpty() )
//            return;
//
//        DateTime now = DateTime.now();
//        String yearMonth = now.getYear() + (now.getMonthOfYear() < 10 ? ("0" + now.getMonthOfYear()) : (now.getMonthOfYear() + ""));
//        DateBean dates = DateConfig.getDates(yearMonth);
//        if ( !dates.isTodayInWork() ) {
//            return;
//        }
//
//        int times = allReminds.size() / 20 + 1;
//        if ( allReminds.size() % 20 == 0 ) {
//            times--;
//        }
//
//        for ( int i = 0; i < times; i++ ) {
//            if ( i == times - 1 ) {
//                //最后一次
//                StringBuilder builder = new StringBuilder();
//                String userIds = "";
//                for ( int j = 0; j < allReminds.size() % 20; j++ ) {
//                    builder.append(allReminds.get(i * 20 + j).getUserId() + ",");
//                }
//                if ( !builder.toString().equals("") ) {
//                    userIds = builder.substring(0, builder.length() - 1);
//                }
//                sendMsg(userIds);
//            } else {
//                //正常情况
//                StringBuilder builder = new StringBuilder();
//                String userIds = "";
//                for ( int j = 0; j < 20; j++ ) {
//                    builder.append(allReminds.get(i * 20 + j).getUserId() + ",");
//                }
//                if ( !builder.toString().equals("") ) {
//                    userIds = builder.substring(0, builder.length() - 1);
//                }
//                sendMsg(userIds);
//            }
//        }
//    }

//    @Scheduled( cron = "0 30 18 * * FRI" )
//    public void scheduledTime() {
//        DateTime now = DateTime.now();
//        String yearMonth = now.getYear() + (now.getMonthOfYear() < 10 ? ("0" + now.getMonthOfYear()) : (now.getMonthOfYear() + ""));
//        DateBean dates = DateConfig.getDates(yearMonth);
//        if ( !dates.isTodayInWork() ) {
//            return;
//        }
//        List<MonthTime> monthTimeComfigs = monthTimeMapper.findAllMonthTimeComfig(now.getYear(), now.getMonthOfYear());
//        if ( monthTimeComfigs == null || monthTimeComfigs.isEmpty() )
//            return;
//        sendBackMsg(monthTimeComfigs);
//    }

    /**
     * 全局发送餐补申请的通知
     */
    public static void sendMealSupportMsg(UserMapper userMapper, String title, String content) {
        //在这里发送消息
        List<String> allUserIds = userMapper.getAllUserIds();
        if ( allUserIds == null || allUserIds.isEmpty() )
            return;

        int times = allUserIds.size() / 20 + 1;
        if ( allUserIds.size() % 20 == 0 ) {
            times--;
        }

        for ( int i = 0; i < times; i++ ) {
            if ( i == times - 1 ) {
                //最后一次
                StringBuilder builder = new StringBuilder();
                String userIds = "";
                for ( int j = 0; j < allUserIds.size() % 20; j++ ) {
                    builder.append(allUserIds.get(i * 20 + j) + ",");
                }
                if ( !builder.toString().equals("") ) {
                    userIds = builder.substring(0, builder.length() - 1);
                }
                sendCommonMsg("申请餐补啦",userIds, title, content);
            } else {
                //正常情况
                StringBuilder builder = new StringBuilder();
                String userIds = "";
                for ( int j = 0; j < 20; j++ ) {
                    builder.append(allUserIds.get(i * 20 + j) + ",");
                }
                if ( !builder.toString().equals("") ) {
                    userIds = builder.substring(0, builder.length() - 1);
                }
                sendCommonMsg("申请餐补啦",userIds, title, content);
            }
        }
    }

    @Async( "processExecutor" )
    public void sendBackMsg(List<MonthTime> monthTimeComfigs) {
        for ( MonthTime monthTimeComfig : monthTimeComfigs ) {
            sendAimUserMsg(monthTimeComfig.getUserId(), monthTimeComfig);
        }
    }

    //发送消息
    private void sendMsg(String userIds) {
        String accessToken = AccessTokenUtil.getToken();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setUseridList(userIds);
        request.setAgentId(199355094l);
        request.setToAllUser(false);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();

        String host = "";
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch ( UnknownHostException e ) {
            e.printStackTrace();
        }
        msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
        msg.getActionCard().setTitle(DateTime.now().toString("HH:mm") + " Mico喊你下班打卡啦！！");
        msg.getActionCard().setMarkdown("### " + DateTime.now().toString("HH:mm") + " Mico喊你下班打卡啦！！");
        msg.getActionCard().setSingleTitle("快去打卡，临走记得关电脑！");
        if ( host == null || host.equals("") ) {
            host = "http://192.168.4.51:8011/notice_ding.html";
        }
        msg.getActionCard().setSingleUrl("http://www.cretinzp.com:8011/notice_ding.html");
        msg.setMsgtype("action_card");
        request.setMsg(msg);

        try {
            client.execute(request, accessToken);
        } catch ( ApiException e ) {
            e.printStackTrace();
        }
    }

    //发送消息
    public static void sendCommonMsg(String tips, String userIds, String title, String content) {
        String accessToken = AccessTokenUtil.getToken();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setUseridList(userIds);
        request.setAgentId(199355094l);
        request.setToAllUser(false);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();

        msg.setMsgtype("markdown");
        msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
        msg.getMarkdown().setTitle(tips);


        msg.getMarkdown().setText("### " + title + "\n##### " + content);
        request.setMsg(msg);

        try {
            client.execute(request, accessToken);
        } catch ( ApiException e ) {
            e.printStackTrace();
        }
    }

    //发送消息
    private void sendAimUserMsg(String userId, MonthTime time) {
        String accessToken = AccessTokenUtil.getToken();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setUseridList(userId);
        request.setAgentId(199355094l);
        request.setToAllUser(false);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();

        msg.setMsgtype("markdown");
        msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
        msg.getMarkdown().setTitle(DateTime.now().toString("HH:mm") + " 每周打卡统计通知");

        String status;
        if ( time.getStatus() == 0 ) {
            //正常
            status = "##### 打卡总览：时间充足，完美！";
        } else {
            //异常
            status = "##### 打卡总览：<font color=\"#ff0000\">时间不足，慎重老铁！</font>";
        }
        if ( time.getErrDays() != null && !time.getErrDays().equals("") ) {
            msg.getMarkdown().setText("### 每周打卡统计通知\n##### 本月截止到今天：\n##### 打卡总时长：" + time.getAllTime() + " \n##### 考勤异常日期：<font color=\"#ff0000\">" + time.getErrDays() + "</font> \n##### 每日打卡时长：" + time.getDailyTime() + " \n" + status);
        } else {
            msg.getMarkdown().setText("### 每周打卡统计通知\n##### 本月截止到今天：\n##### 打卡总时长：" + time.getAllTime() + " \n##### 每日打卡时长：" + time.getDailyTime() + " \n" + status);
        }
        request.setMsg(msg);

        try {
            client.execute(request, accessToken);
        } catch ( ApiException e ) {
            e.printStackTrace();
        }
    }
}