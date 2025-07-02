package com.wzc.mq.mqserver.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * 表示一条要传递的消息,并且需要实现 Serializable 接口
 */
@Data
public class Message implements Serializable {

//    元数据信息
    private BasicProperties basicProperties = new BasicProperties();

//    消息主题
    private byte[] body;

//    在文件中通过下列偏移量定位到文件具体的某一条消息 [offsetBeg,offsetEnd)
//    并且这两个属性不需要被序列化，被写入文件之后，所在的位置就固定了，不需要单独存储，使用 transient 关键字让某一个属性不被序列化
//    每个消息的开始偏移量
    private transient long offsetBeg = 0;
//    每个消息结束偏移量
    private transient long offsetEnd = 0;

//    逻辑删除位 0x1 : 有效    0x0 : 无效
    private byte isValid = 0x1;

    /**
     * 创建一个工厂方法
     * @return Message 对象
     */
    public static Message createMessageWithId(String routingKey,BasicProperties basicProperties,byte[] body){
        Message message = new Message();
        if(basicProperties != null) message.setBasicProperties(basicProperties);
        message.setMessageId("M-"+UUID.randomUUID());
        message.setRoutingKey(routingKey);
        message.body = body;
        return message;
    }

    /**
     * 获取消息 Id
     * @return 返回消息 ID
     */
    public String getMessageId(){
        return basicProperties.getMessageId();
    }

    /**
     * 设置消息 ID
     * @param messageId 要设置的消息 ID
     */
    public void setMessageId(String messageId){
        basicProperties.setMessageId(messageId);
    }

    /**
     * 获取消息的 routingKey
     * @return routingKey
     */
    public String getRoutingKey(){
        return basicProperties.getRoutingKey();
    }

    /**
     * 设置消息的 routingKey
     * @param routingKey 需要被设置的 routingKey
     */
    public void setRoutingKey(String routingKey){
        basicProperties.setRoutingKey(routingKey);
    }

    /**
     * 获取消息的持久化方式
     * @return 持久化方式 ：1 -> 持久   2 -> 不持久
     */
    public int getDeliverModel(){
        return basicProperties.getDeliverModel();
    }

    /**
     * 设置消息的持久化方式
     * @param mode 1 -> 持久   2 -> 不持久
     */
    public void setDeliverModel(int mode){
        basicProperties.setDeliverModel(mode);
    }

}
