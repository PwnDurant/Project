package com.zqq.common.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Configuration
public class Mail {

    private JavaMailSender javaMailSender;

    private MailProperties mailProperties;

    public Mail(JavaMailSender javaMailSender,MailProperties mailProperties) {
        this.javaMailSender = javaMailSender;
        this.mailProperties=mailProperties;
    }

    public void send(String to,String subject,String content) throws MessagingException, UnsupportedEncodingException {
//        创建一个邮件消息
        MimeMessage message=javaMailSender.createMimeMessage();
//        创建MimeMessageHelper
        MimeMessageHelper helper=new MimeMessageHelper(message,false);
        String personal= Optional.ofNullable(mailProperties.getProperties().get("personal"))
                .orElse(mailProperties.getUsername());
//        发件人邮箱和姓名
        helper.setFrom(mailProperties.getUsername(),personal);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content,true);

        javaMailSender.send(message);


    }

}
