package com.chase.rocket.producer;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import com.alibaba.rocketmq.remoting.exception.RemotingException;


import java.io.UnsupportedEncodingException;

public class demo1 {
    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException {
//        Instantiate with a Producer group name 使用生产组名称实化
        DefaultMQProducer producer = new DefaultMQProducer("test");
        producer.setNamesrvAddr("192.168.100.131:9876");
//        launch the instance 启动实例
        producer.start();
        for (int i = 0; i < 100; i++) {
//            创建一个消息实例，指定主题，标签和消息体，并把要传输的字符串转换为 UTF-8 编码的字节数组进行传输
//            RocketMQ 底层通讯，消息传输都是用了 byte[],为了统一编码方式封装了 RemotingHelper.DEFAULT_CHARSET,确保所有编码都是 utf-8
            Message message = new Message("TopicTest", "TagA", ("Hello RocketMQ" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
//            开始将消息发送给某个 broker
            SendResult sendResult = producer.send(message);
            System.out.printf("%s%n",sendResult);
        }
//        当生产者实例不再使用时关闭
        producer.shutdown();
    }
}
