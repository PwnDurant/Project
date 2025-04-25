package com.zqq.javaailangchain4j;


import com.zqq.javaailangchain4j.assistant.Assistant;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AIServiceTest {

    @Autowired
    private QwenChatModel qwenChatModel;

    @Test
    public void test(){
        Assistant assistant = AiServices.create(Assistant.class, qwenChatModel);
        String chat = assistant.chat("你是谁？");
        System.out.println(chat);
    }

    @Autowired
    private Assistant assistant;

    @Test
    public void testA(){
        System.out.println(assistant.chat("我是谁"));
    }

}
