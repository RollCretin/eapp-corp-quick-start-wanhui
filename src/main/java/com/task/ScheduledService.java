package com.task;

import com.config.DateBean;
import com.config.DateConfig;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.mapper.RemindMapper;
import com.model.domain.Remind;
import com.taobao.api.ApiException;
import com.util.AccessTokenUtil;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Component
public class ScheduledService {
    @Autowired
    private RemindMapper remindMapper;

    @Scheduled( cron = "0 28 18 * * *" )
    public void scheduled() {
        //在这里发送消息
        List<Remind> allReminds = remindMapper.findAllReminds();
        if ( allReminds == null || allReminds.isEmpty() )
            return;

        DateTime now = DateTime.now();
        String yearMonth = now.getYear() + (now.getMonthOfYear() < 10 ? ("0" + now.getMonthOfYear()) : (now.getMonthOfYear() + ""));
        DateBean dates = DateConfig.getDates(yearMonth);
        if ( !dates.isTodayInWork() ) {
            return;
        }

        int times = allReminds.size() / 20 + 1;
        if ( allReminds.size() % 20 == 0 ) {
            times--;
        }

        for ( int i = 0; i < times; i++ ) {
            if ( i == times - 1 ) {
                //最后一次
                StringBuilder builder = new StringBuilder();
                String userIds = "";
                for ( int j = 0; j < allReminds.size() % 20; j++ ) {
                    builder.append(allReminds.get(i * 20 + j).getUserId() + ",");
                }
                if ( !builder.toString().equals("") ) {
                    userIds = builder.substring(0, builder.length() - 1);
                }
                sendMsg(userIds);
            } else {
                //正常情况
                StringBuilder builder = new StringBuilder();
                String userIds = "";
                for ( int j = 0; j < 20; j++ ) {
                    builder.append(allReminds.get(i * 20 + j).getUserId() + ",");
                }
                if ( !builder.toString().equals("") ) {
                    userIds = builder.substring(0, builder.length() - 1);
                }
                sendMsg(userIds);
            }
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
}