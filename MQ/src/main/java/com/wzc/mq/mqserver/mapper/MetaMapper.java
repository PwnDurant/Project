package com.wzc.mq.mqserver.mapper;

import com.wzc.mq.mqserver.core.Binding;
import com.wzc.mq.mqserver.core.Exchange;
import com.wzc.mq.mqserver.core.MSGQueue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 提供三个核心建表方法
 * 以及对其进行插入和删除操作
 */
@Mapper
public interface MetaMapper {

//    创建交换机表
    void createExchangeTable();

//    创建消息队列表
    void createQueueTable();

//    创建绑定关系表
    void createBindingTable();

//    插入交换机
    void insertExchange(Exchange exchange);

//    删除交换机
    void deleteExchange(String exchangeName);

//    插入队列
    void insertQueue(MSGQueue queue);

//    删除队列
    void deleteQueue(String queueName);

//    新增绑定关系
    void insertBinding(Binding binding);

//    删除绑定关系
    void deleteBinding(Binding binding);

//    查询所有交换机
    List<Exchange> selectAllExchange();

//    查询所有消息队列
    List<MSGQueue> selectAllQueue();

//    查询所有绑定关系
    List<Binding> selectAllBindings();

}
