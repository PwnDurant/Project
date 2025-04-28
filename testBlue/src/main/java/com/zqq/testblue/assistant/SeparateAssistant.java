package com.zqq.testblue.assistant;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;



import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

@AiService(
        wiringMode = EXPLICIT,
        chatModel = "qwenChatModel",
//        chatMemory = "chatMemoryDIY",
        chatMemoryProvider = "chatMemoryProviderDIY" , //创建隔离记忆
        tools = "calcTool", //配置Tools
        contentRetriever = "contentRetrieverChasePincone"
)
public interface SeparateAssistant {

    /**
     * 分离聊天记录
     * @param memoryId 聊天id
     * @param userMessage 用户消息
     * @return
     */
//    @SystemMessage("你是我的好朋友，请用东北话回答.今天是{{current_date}}") //系统消息提示词
//    @SystemMessage 的内容将在后台转换为 SystemMessage 对象，并与 UserMessage 一起发送给大语言模型（LLM）。
//    SystemMessaged的内容只会发送给大模型一次。如果你修改了SystemMessage的内容，新的SystemMessage会被发送给大模型，之前的聊天记忆会失效。
    @SystemMessage(fromResource = "prompt.txt") //系统消息提示词
    String chat(@MemoryId int memoryId, @UserMessage  String userMessage);

    @UserMessage("你是我的好朋友，请用粤语回答问题。{{message}}")
    String chat2(@MemoryId int memoryId, @V ("message")  String userMessage);

    @SystemMessage(fromResource = "prompt1.txt")
    String chat3(
            @MemoryId int memoryId,
            @UserMessage String userMessage,
            @V("username") String username,
            @V("age") int age);




}
