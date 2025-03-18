package com.zqq.user;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void send(){
        rabbitTemplate.convertAndSend("","hello","helloRabbit");
    }
}
