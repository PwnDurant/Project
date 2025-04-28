package com.zqq.testblue;


import com.zqq.testblue.domain.ChatMessages;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@SpringBootTest
public class MongoCRUD {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testInsert(){
        ChatMessages chatMessages =new ChatMessages();
        chatMessages.setContent("聊天记录列表");
        mongoTemplate.insert(chatMessages);
    }

    @Test
    public void testFindById(){
        System.out.println(mongoTemplate.findById("680f33acd5707e53f3fd5c79", ChatMessages.class));
    }

//    修改或者新增
    @Test
    public void testUpdate(){
        Criteria criteria=Criteria.where("_id").is("680f33acd5707e53f3fd5c79");
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("content","新的聊天记录");
//        修改或新增
        mongoTemplate.upsert(query,update, ChatMessages.class);
    }

    @Test
    public void testUpdate1(){
//        Criteria criteria=Criteria.where("memoryId").is("100");
        Criteria criteria=Criteria.where("_id").is("100");
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("content","新的聊天记录");
//        修改或新增
        mongoTemplate.upsert(query,update, ChatMessages.class);
    }


    @Test
    public void testDelete(){
        Criteria criteria = Criteria.where("memoryId").is("100");
        Query query = new Query(criteria);
        mongoTemplate.remove(query, ChatMessages.class);
    }

}
