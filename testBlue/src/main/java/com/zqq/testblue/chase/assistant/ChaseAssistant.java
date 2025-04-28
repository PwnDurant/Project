package com.zqq.testblue.chase.assistant;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
//        chatModel = "qwenChatModel",
        streamingChatModel = "qwenStreamingChatModel",
        chatMemoryProvider = "chatMemoryProviderChase",
        contentRetriever = "contentRetrieverChasePincone"
)
public interface ChaseAssistant {

    @SystemMessage(fromResource = "promptChase.txt")
    Flux<String> chat(@MemoryId Long memoryId, @UserMessage String userMessage);

}
