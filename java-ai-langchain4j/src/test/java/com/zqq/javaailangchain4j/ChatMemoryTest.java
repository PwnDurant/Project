package com.zqq.javaailangchain4j;


import com.zqq.javaailangchain4j.assistant.Assistant;
import com.zqq.javaailangchain4j.assistant.MemoryChatAssistant;
import com.zqq.javaailangchain4j.assistant.SeparateChatAssistant;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class ChatMemoryTest {

    @Autowired
    private Assistant assistant;

    @Test
    public void testChatMemory(){
        System.out.println(assistant.chat("我是环环"));
        System.out.println(assistant.chat("我是谁？"));
    }


    @Autowired
    private QwenChatModel qwenChatModel;
    @Test
    public void testChatMemory2() {
//第一轮对话
        UserMessage userMessage1 = UserMessage.userMessage("我是环环");
        ChatResponse chatResponse1 = qwenChatModel.chat(userMessage1);
        AiMessage aiMessage1 = chatResponse1.aiMessage();
//输出大语言模型的回复
        System.out.println(aiMessage1.text());
//第二轮对话
        UserMessage userMessage2 = UserMessage.userMessage("你知道我是谁吗");
        ChatResponse chatResponse2 = qwenChatModel.chat(Arrays.asList(userMessage1,
                aiMessage1, userMessage2));
        AiMessage aiMessage2 = chatResponse2.aiMessage();
//输出大语言模型的回复
        System.out.println(aiMessage2.text());
    }

    @Test
    public void testChatMemory3() {

        MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.withMaxMessages(10);

        Assistant build = AiServices.builder(Assistant.class)
                .chatLanguageModel(qwenChatModel)
                .chatMemory(messageWindowChatMemory)
                .build();

        System.out.println(build.chat("我叫欢欢"));
        System.out.println(build.chat("我是谁"));

    }

    @Autowired
    private MemoryChatAssistant memoryChatAssistant ;

    @Test
    public void testChatMemory4() {

        System.out.println(memoryChatAssistant.chat("我叫欢欢"));
        System.out.println(memoryChatAssistant.chat("我是谁"));

    }


    @Autowired
    private SeparateChatAssistant separateChatAssistant ;

    @Test
    public void testChatMemory5() {

        System.out.println(separateChatAssistant.chat(1,"我叫欢欢"));
        System.out.println(separateChatAssistant.chat(1,"我是谁"));
        System.out.println(separateChatAssistant.chat(2, "我是谁"));
    }


}
