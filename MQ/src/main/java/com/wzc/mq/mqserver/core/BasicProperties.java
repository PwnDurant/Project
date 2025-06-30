package com.wzc.mq.mqserver.core;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息的元数据（属性）信息
 */
@Data
public class BasicProperties implements Serializable {

//    消息的唯一标识
    private String messageId;

//    用来与 bingingKey 做匹配的，并且在不同类型交换机下的 routingKey 的含义也不一样
//    DIRECT : 表示要转发的队列名称
//    FANOUT : 无意义（不使用）
//    TOPIC  : 用来与 bindingKey 做匹配，发送给符合要求的队列
    private String routingKey;

//    表示消息是否持久化：1:不持久化   2:持久化
    private int deliverModel = 1;

}
