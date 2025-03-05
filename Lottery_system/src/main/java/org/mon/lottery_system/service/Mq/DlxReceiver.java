package org.mon.lottery_system.service.Mq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.mon.lottery_system.common.config.DirectRabbitConfig.*;

@RabbitListener(queues = DLX_QUEUE_NAME)
@Component
@Slf4j
public class DlxReceiver {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitHandler
    public void process(Map<String,String> map){
//        死信队列的处理方法
        log.info("处理异常消息！");
//        rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTING,map);

//        1，接收到异常消息，可以将异常消息存到数据库表中
//        2，存放后，当前异常消息消费完成，死信队列消息处理完成，但是异常消息被我们持久化储存到表中
//        3，解决异常
//        4，完成脚本任务，判断异常消息表中是否存在数据，如果存在，标识有消息未完成，此时处理消息
//        5，处理消息，将消息发送给普通队列进行完成处理
    }
}
