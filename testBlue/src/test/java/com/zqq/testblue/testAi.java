package com.zqq.testblue;

import com.zqq.testblue.assistant.AssistantQwen;
import com.zqq.testblue.assistant.SeparateAssistant;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class testAi {

    @Test
    public void testDemo(){
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();
        String answer = model.chat("Say 'Hello World'");
        System.out.println(answer); // Hello World
    }

    @Autowired
    private OpenAiChatModel openAiChatModel;

    @Autowired
    private QwenChatModel qwenChatModel;

    @Test
    public void testQwen(){
        System.out.println(qwenChatModel.chat("你好，你是什么模型"));
    }

    @Test
    public void testSpringBoot(){
        System.out.println(openAiChatModel.chat("你好,你是什么模型"));
    }

    @Test
    public void testChat(){
        AssistantQwen assistantQwen = AiServices.create(AssistantQwen.class, qwenChatModel);
        System.out.println(assistantQwen.chat("你好，你是什么模型"));
    }

    @Autowired
    private AssistantQwen assistantQwen;

    @Test
    public void testAssistant(){
        System.out.println(assistantQwen.chat("你好，你叫什么名字"));
    }

    @Test
    public void testChatMemory(){
        //        封装用户输入
        UserMessage userMessage1 = UserMessage.userMessage("我是欢欢");
//        取出模型输出
        ChatResponse chatResponse1 = qwenChatModel.chat(userMessage1);
        AiMessage aiMessage1 = chatResponse1.aiMessage();
//        输出大模型回复
        System.out.println(aiMessage1.text());

        UserMessage userMessage2 = UserMessage.userMessage("你知道我是谁吗");
        ChatResponse chatResponse2 = qwenChatModel.chat(Arrays.asList(userMessage1, aiMessage1, userMessage2));
        AiMessage aiMessage2 = chatResponse2.aiMessage();
        System.out.println(aiMessage2.text());
    }


    @Test
    public void testMemory3(){
//        创建chatMemory
        MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.withMaxMessages(10);

//        创建AIService,带有聊天记忆的assistant
        AssistantQwen assistant = AiServices
                .builder(AssistantQwen.class)
                .chatLanguageModel(qwenChatModel)
                .chatMemory(messageWindowChatMemory)
                .build();
//        调用service接口
        System.out.println(assistant.chat("我是Chase"));
        System.out.println(assistant.chat("我是谁"));

    }

    @Test
    public void testChatMemory3(){
        System.out.println(assistantQwen.chat("我是Chase"));
        System.out.println(assistantQwen.chat("我是谁"));
    }

    @Autowired
    private SeparateAssistant separateAssistant;

    @Test
    public void testVivo(){
        System.out.println(separateAssistant.chat(1, "你好"));
    }

    @Test
    public void testChatMemorySeparate(){
        System.out.println(separateAssistant.chat(1,"我是Chase"));
        System.out.println(separateAssistant.chat(1,"我是谁"));
        System.out.println(separateAssistant.chat(2,"我是谁"));
    }

    @Test
    public void testUserMessage(){
        System.out.println(assistantQwen.chat("我是欢欢"));
    }

}
