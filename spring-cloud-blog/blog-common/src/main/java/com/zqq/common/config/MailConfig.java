package com.zqq.common.config;

import com.zqq.common.utils.Mail;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MailConfig {
    @Bean
    @ConditionalOnProperty(prefix = "spring.mail",name = "username")
    public Mail mail(JavaMailSender javaMailSender, MailProperties mailProperties){
        return new Mail(javaMailSender, mailProperties);
    }
}
