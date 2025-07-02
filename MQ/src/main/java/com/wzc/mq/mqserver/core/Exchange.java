package com.wzc.mq.mqserver.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 交换机
 */
@Data
public class Exchange {

    private static final ObjectMapper obj = new ObjectMapper();

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

    public String getArguments(){
        try{
            return obj.writeValueAsString(arguments);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return "{}";
    }

    public void setArguments(String arguments){
        try{
            this.arguments = obj.readValue(arguments, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setArguments(String key,Object value){
        arguments.put(key, value);
    }

    public Object getArguments(String key){
        return  arguments.get(key);
    }
}
