package com.wzc.mq.mqserver.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 表示一个队列和交换机之间的关联关系
 */
@Data
@AllArgsConstructor
public class Binding {

//    绑定的交换机的名称
    private String exchangeName;

//    绑定的消息队列名称
    private String queueName;

//    绑定之间的关系
    private String bindingKey;
}
