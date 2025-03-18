package com.zqq.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.UnsupportedEncodingException;

@SpringBootTest
public class MailTest {

    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    void send() throws MessagingException, UnsupportedEncodingException {
//        创建一个邮件消息
        MimeMessage message=javaMailSender.createMimeMessage();
//        创建MimeMessageHelper
        MimeMessageHelper helper=new MimeMessageHelper(message,false);

//        发件人邮箱和姓名
        helper.setFrom("1781389019@qq.com","zqq");
        helper.setTo("1781389019@qq.com");
        helper.setSubject("Test");
        helper.setText("注册成功",true);

        javaMailSender.send(message);


    }


}
