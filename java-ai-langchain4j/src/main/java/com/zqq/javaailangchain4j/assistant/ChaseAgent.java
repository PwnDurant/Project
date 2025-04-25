package com.zqq.javaailangchain4j.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;


@AiService(
        wiringMode = EXPLICIT,
        streamingChatModel = "qwenStreamingChatModel",
        chatMemoryProvider = "chatMemoryProviderChase",
        tools = "appointmentTools" ,//tools配置
        contentRetriever = "contentRetrieverXiaozhiPincone"
)
public interface ChaseAgent {

    @SystemMessage(fromResource = "ChasePrompt.txt")
    Flux<String> chat(@MemoryId Long memoryId, @UserMessage String userMessage);
//    String chat(@MemoryId Long memoryId, @UserMessage String userMessage);

}
