package com.wzc.mq.mqserver.core;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 交换机
 */
@Data
public class Exchange {

//    此处使用 name 作为交换机的身份标识（唯一的）
    private String name;

//    交换机类型: DIRECT,FANOUT,TOPIC
    private ExchangeType type = ExchangeType.DIRECT;

//    该交换机是否需要持久化存储
    private boolean durable = false;

//    如果该交换机没有人使用的话就自动删除（后续拓展）
    private boolean autoDelete = false;

//    创建交换机时指定的一些额外的选项（后续拓展）
    private Map<String,Object> arguments =  new HashMap<>();
}
