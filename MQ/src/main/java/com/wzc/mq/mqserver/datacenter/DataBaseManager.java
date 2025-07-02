package com.wzc.mq.mqserver.datacenter;


import com.wzc.mq.MqApplication;
import com.wzc.mq.mqserver.core.Binding;
import com.wzc.mq.mqserver.core.Exchange;
import com.wzc.mq.mqserver.core.ExchangeType;
import com.wzc.mq.mqserver.core.MSGQueue;
import com.wzc.mq.mqserver.mapper.MetaMapper;

import java.io.File;
import java.util.List;

/**
 * 封装数据库的操作
 */
public class DataBaseManager {

//    通过 ConfigurableApplicationContext 这个类取出上下文对象
    private MetaMapper metaMapper;

    /**
     * 针对数据库进行初始化操作
     */
    public void init(){
        metaMapper = MqApplication.context.getBean(MetaMapper.class);
        if(!checkDBExists()){
//            数据库不存在就进行建库操作，先创建一个 data 目录
            File dataDir = new File("./data");
            dataDir.mkdirs();
//            创建数据表
            createTable();
//            插入默认数据
            createDefaultData();
            System.out.println("【DataBaseManager】数据库初始化完成！");
        }else{
//            数据库存在的话就不用操作
            System.out.println("【DataBaseManager】数据库已经存在！");
        }
    }

    /**
     * 删除数据库
     */
    public void deleteDB(){
        File file = new File("./data/meta.db");
        boolean delete = file.delete();
        if(delete) System.out.println("【DataBaseManager】删除数据库文件成功！");
        else System.out.println("【DataBaseManager】删除数据库文件失败！");
        File file1 = new File("./data");
//        使用 delete 删除目录，需要保证目录是空的
        boolean delete1 = file1.delete();
        if(delete1) System.out.println("【DataBaseManager】删除数据库目录成功！");
        else System.out.println("【DataBaseManager】删除数据库目录失败！");
    }

//    主要是创建一个默认交换机
    private void createDefaultData() {
        Exchange exchange = new Exchange();
        exchange.setName("");
        exchange.setType(ExchangeType.DIRECT);
        exchange.setDurable(true);
        exchange.setAutoDelete(false);
        metaMapper.insertExchange(exchange);
        System.out.println("【DataBaseManager】创建初始数据完成！");
    }

    private void createTable() {
        metaMapper.createExchangeTable();
        metaMapper.createQueueTable();
        metaMapper.createBindingTable();
        System.out.println("【DataBaseManager】创建表完成");
    }

    private boolean checkDBExists() {
        File file = new File("./data/meta.db");
        return file.exists();
    }

    /**
     * 下面是其他数据库的封装
     */
    public void insertExchange(Exchange exchange){
        metaMapper.insertExchange(exchange);
    }
    public List<Exchange> selectAllExchanges(){
        return metaMapper.selectAllExchange();
    }
    public void deleteExchange(String exchangeName){
        metaMapper.deleteExchange(exchangeName);
    }

    public void insertQueue(MSGQueue queue){
        metaMapper.insertQueue(queue);
    }
    public List<MSGQueue> selectAllQueue(){
        return metaMapper.selectAllQueue();
    }
    public void deleteQueue(String queueName){
        metaMapper.deleteQueue(queueName);
    }

    public void insertBinding(Binding binding){
        metaMapper.insertBinding(binding);
    }
    public List<Binding> selectAllBindings(){
        return metaMapper.selectAllBindings();
    }
    public void deleteBinding(Binding binding){
        metaMapper.deleteBinding(binding);
    }


}
