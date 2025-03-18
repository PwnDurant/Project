package com.zqq.user.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HelloQueueRabbit {

    @RabbitListener(queues = "hello")
    public void handler(String m){
        System.out.println("收到消息"+m);
    }
}
