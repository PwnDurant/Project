package com.zqq.javaailangchain4j;

import com.zqq.javaailangchain4j.assistant.MemoryChatAssistant;
import com.zqq.javaailangchain4j.assistant.SeparateChatAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PromptTest {

    @Autowired
    private SeparateChatAssistant separateChatAssistant;

    @Test
    public void testSystemMessage() {
        String answer = separateChatAssistant.chat(4,"今天几号");
        System.out.println(answer);
    }

    @Autowired
    private MemoryChatAssistant memoryChatAssistant;
    @Test
    public void testUserMessage() {
        String answer = memoryChatAssistant.chat("我是环环");
        System.out.println(answer);
        String answer1 = memoryChatAssistant.chat("我是18了");
        System.out.println(answer1);
        String answer2 = memoryChatAssistant.chat("你知道我是谁吗");
        System.out.println(answer2);
    }

    @Test
    public void testUserInfo(){
//        从数据库中获取用户信息
        String username="翠花";
        int age=18;

        System.out.println(separateChatAssistant.chat3(20, "我是谁，我多大了", username, age));
    }
}
