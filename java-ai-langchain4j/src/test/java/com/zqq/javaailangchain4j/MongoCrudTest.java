package com.zqq.javaailangchain4j;

import com.zqq.javaailangchain4j.bean.ChatMessages;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@SpringBootTest
public class MongoCrudTest {

    @Autowired
    private MongoTemplate mongoTemplate;

//    @Test
//    public void testInsert(){
//        mongoTemplate.insert(new ChatMessages(1L,"聊天记录"));
//    }

    @Test
    public void testInsert1(){
        ChatMessages chatMessages = new ChatMessages();
        chatMessages.setContent("列表");
        mongoTemplate.insert(chatMessages);
    }

    @Test
    public void testFindById(){
        ChatMessages byId = mongoTemplate.findById("680a219692d3cf48b650dde0", ChatMessages.class);
        System.out.println(byId);
    }

    @Test
    public void testUpdate(){
        Criteria id = Criteria.where("_id").is("100");
        Query query = new Query(id);
        Update update = new Update();
        update.set("content","新的");
        mongoTemplate.upsert(query,update,ChatMessages.class);
    }

    @Test
    public void testDelete(){
        Criteria id = Criteria.where("_id").is("100");
        Query query = new Query(id);
        mongoTemplate.remove(query,ChatMessages.class);
    }

}
