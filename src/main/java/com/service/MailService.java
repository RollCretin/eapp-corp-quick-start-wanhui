package com.service;

import com.config.Constant;
import com.util.StringUtils;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MailService {
    private static final Logger LOG = LoggerFactory.getLogger(MailService.class);

    @Value( "${spring.mail.username}" )
    private String SEND_USER_ADDR;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送简单邮件
     *
     * @param receive 收件人
     * @param obj     发送主题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String receive, String obj, String content) {
        if ( StringUtils.isEmpty(content) || StringUtils.isEmpty(receive) )
            return;//不发送空邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SEND_USER_ADDR);
        if ( receive.contains(";") )
            message.setTo(receive.split(";"));
        else
            message.setTo(receive);
        message.setSubject(obj);
        message.setText(content);
        try {
            mailSender.send(message);
            LOG.info("Simple mail send success!");
        } catch ( Exception e ) {
            LOG.error("sendSimpleMail ERROR!", e);
        }
    }

    private StringBuilder strBuilder;

    /**
     * 发送html邮件 多列表单的形式
     *
     * @param receive 收件人
     * @param obj     发送主题(题目)
     * @param content 邮件内容
     */
    public void sendHtmlMailByList(String receive, String obj, List<Map> content) {
        if ( content.isEmpty() || StringUtils.isEmpty(receive) || null == obj )
            return;
        MimeMessage msg = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8"); //解决乱码问题
            helper.setFrom(SEND_USER_ADDR);
            if ( receive.contains(";") )
                helper.setTo(receive.split(";"));
            else
                helper.setTo(receive);
            helper.setSubject(obj);
            strBuilder = new StringBuilder();
            strBuilder.append("<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body style=\"padding:3% 2%;\">");
            strBuilder.append("<h2>This message is automatically sent to the system.</h2>");
            strBuilder.append("<h2>Send Date by " + DateTime.now().toString(Constant.DATE_FORMAT) + "</h2>");
            strBuilder.append("<h2>The following is the details:</h2>");
            strBuilder.append("<table border=\"2px solid red\" width=\"100%\">");

            //头
            strBuilder.append("<thead style=\"background-color: #aea2e2;\">");
            strBuilder.append("<tr>");
            Object[] st = content.get(0).keySet().toArray();
            for ( int i = 0; i < st.length; i++ )
                strBuilder.append("<th>" + st[i] + "</th>");
            strBuilder.append("</tr>");
            strBuilder.append("</thead>");

            //体
            strBuilder.append("<tbody>");
            for ( Map item : content ) {
                strBuilder.append("<tr>");
                for ( Object str : st )
                    strBuilder.append("<td>" + item.get(str) + "</td>");
                strBuilder.append("</tr>");
            }
            strBuilder.append("</tbody>");

            strBuilder.append("</table>");
            strBuilder.append("<h3 style=\"text-align:right\">Best wishes</h3>");
            strBuilder.append("</body></html>");
            //LOG.info(strBuilder.toString());
            helper.setText(strBuilder.toString(), true);
        } catch ( Exception e ) {
            LOG.error("sendHtmlMail ERROR:", e);
        }
        mailSender.send(msg);
    }


    /**
     * 发送html邮件 单列记录形式
     *
     * @param receive 收件人
     * @param obj     发送主题(题目)
     * @param content 邮件内容
     */
    public void sendHtmlMailByItem(String receive, String obj, List<String> content) {
        if ( content.isEmpty() || StringUtils.isEmpty(receive) || null == obj )
            return;
        MimeMessage msg = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8"); //解决乱码问题
            helper.setFrom(SEND_USER_ADDR);
            if ( receive.contains(";") )
                helper.setTo(receive.split(";"));
            else
                helper.setTo(receive);
            helper.setSubject(obj);
            strBuilder = new StringBuilder();
            strBuilder.append("<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body style=\"padding:3% 2%;\">");
            strBuilder.append("<h3>This message is automatically sent to the system.</h3>");
            strBuilder.append("<h3>Send Date by " + DateTime.now().toString(Constant.DATE_FORMAT) + "</h3>");
            strBuilder.append("<h3>The following is the details:</h3>");
            strBuilder.append("<table border=\"2px solid red\" width=\"100%\">");

            //头
            strBuilder.append("<thead style=\"background-color: #aea2e2;\">");

            strBuilder.append("<th>" + obj.toUpperCase() + " DETAIL</th>");
            strBuilder.append("</thead>");

            //体
            strBuilder.append("<tbody>");
            for ( String item : content ) {
                strBuilder.append("<tr><td>" + item + "</td></tr>");
            }
            strBuilder.append("</tbody>");

            strBuilder.append("</table>");
            strBuilder.append("<h3 style=\"text-align:right;font-weight:normal;\">Best wishes</h3>");
            strBuilder.append("</body></html>");
            LOG.info(strBuilder.toString());
            helper.setText(strBuilder.toString(), true);
        } catch ( Exception e ) {
            LOG.error("sendHtmlMail ERROR:", e);
        }
        mailSender.send(msg);
    }
}