package com.zqq.user.listener;

import com.rabbitmq.client.Channel;
import com.zqq.common.constant.Constants;
import com.zqq.common.utils.JsonUtil;
import com.zqq.common.utils.Mail;
import com.zqq.user.dataobject.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class UserQueueListener {

    @Autowired
    private Mail mail;


//    @RabbitListener(queues = Constants.USER_QUEUE_NAME)
    @RabbitListener(bindings = @QueueBinding(value=@Queue(value = Constants.USER_QUEUE_NAME,durable = "true"),
    exchange = @Exchange(value = Constants.USER_EXCHANGE_NAME,durable = "true",type = ExchangeTypes.FANOUT)))
    public void handler(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try{
            String body=new String(message.getBody());
            log.info("收到用户信息，body:{}",body);
//            TODO 发送注册成功的邮件
            UserInfo userInfo= JsonUtil.parseJson(body, UserInfo.class);
            mail.send(userInfo.getEmail(), "恭喜加入Chase社区",builderContent(userInfo.getUserName()));
            channel.basicAck(deliveryTag,true);
        }catch (Exception e){
            channel.basicNack(deliveryTag,true,true);
            log.error("邮件发送失败,e:{}",e);
        }
    }

    private String builderContent(String username){
        StringBuilder builder=new StringBuilder();
        builder.append("尊敬的").append(username).append(",您好！").append("<br/>");
        builder.append("感谢您注册为博客社区的一员，感谢您加入我们的大家庭!<br/>");
        builder.append("您的注册信息如下:用户名:").append(username).append("<br/>");
        builder.append("为了您的账户安全，请妥善保管您的用户信息");
        return builder.toString();
    }
}
