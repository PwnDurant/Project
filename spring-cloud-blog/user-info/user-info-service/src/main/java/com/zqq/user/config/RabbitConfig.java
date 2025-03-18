package com.zqq.user.config;

import com.zqq.common.constant.Constants;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean("helloQueue")
    public Queue queue(){
        return QueueBuilder.durable("hello").build();
    }

    @Bean("userQueue")
    public Queue userQueue(){
        return QueueBuilder.durable(Constants.USER_QUEUE_NAME).build();
    }

    @Bean("userExchange")
    public FanoutExchange userExchange(){
        return ExchangeBuilder.fanoutExchange(Constants.USER_EXCHANGE_NAME).durable(true).build();
    }


    @Bean("userBinding")
    public Binding userBinding(@Qualifier("userQueue") Queue userQueue, @Qualifier("userExchange") FanoutExchange userExchange){
            return BindingBuilder.bind(userQueue).to(userExchange);
        }

}
