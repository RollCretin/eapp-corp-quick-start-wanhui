package com.service;

import com.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Service
public class MailService {
    private static final Logger LOG = LoggerFactory.getLogger(MailService.class);

    @Value( "${spring.mail.username}" )
    private String SEND_USER_ADDR;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送带附件的邮件
     *
     * @param to       收件人
     * @param subject  邮件主题
     * @param msg      邮件内容
     * @param filename 附件地址
     * @return
     * @throws GeneralSecurityException
     */
    public boolean sendMail(String to, String subject, String msg, String filename) {
        if ( StringUtils.isEmpty(to) ) {
            return false;
        }
        if ( StringUtils.isEmpty(subject) ) {
            return false;
        }
        if ( StringUtils.isEmpty(msg) ) {
            msg = "";
        }
        // 创建默认的 MimeMessage 对象
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(SEND_USER_ADDR);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(msg, true);
            // 创建消息部分
            BodyPart messageBodyPart = new MimeBodyPart();
            // 消息
            messageBodyPart.setText(msg);
            // 创建多重消息
            Multipart multipart = new MimeMultipart();
            // 设置文本消息部分
            multipart.addBodyPart(messageBodyPart);
            if ( filename != null ) {
                multipart = setFile(filename, multipart);
            }
            // 发送完整消息
            message.setContent(multipart);
            // 发送消息
            mailSender.send(message);
            return true;
        } catch ( MessagingException e ) {
            e.printStackTrace();
        }
        return false;
    }

    private Multipart setFile(String filename, Multipart multipart) throws MessagingException {
        // 附件部分
        BodyPart messageBodyPart = new MimeBodyPart();
        // 设置要发送附件的文件路径
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        // messageBodyPart.setFileName(filename);
        // 处理附件名称中文（附带文件路径）乱码问题
        messageBodyPart.setFileName(source.getName());
        multipart.addBodyPart(messageBodyPart);
        return multipart;
    }
}