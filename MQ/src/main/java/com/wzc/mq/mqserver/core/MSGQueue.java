package com.wzc.mq.mqserver.core;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息队列
 */
@Data
public class MSGQueue {

//    标识队列的唯一身份标识
    private String name;

//    队列是否持久化
    private boolean durable;

//    这个属性为 true，表示这个队列只能被一个消费者所使用，如果为 false 表示大家都可以使用（后续拓展）
    private boolean exclusive;

//    如果该队列没有人使用的话就自动删除（后续拓展）
    private boolean autoDelete = false;

//    创建队列时指定的一些额外的选项（后续拓展）
    private Map<String,Object> arguments =  new HashMap<>();

}
