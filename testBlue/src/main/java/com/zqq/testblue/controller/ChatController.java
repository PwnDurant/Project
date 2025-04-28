package com.zqq.testblue.controller;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChatController {

    QwenChatModel chatLanguageModel;

    public ChatController(QwenChatModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    @GetMapping("/chat")
    public String model(@RequestParam(value = "message", defaultValue = "Hello") String message) {
        return chatLanguageModel.chat(message);
    }

//    @Autowired
//    StreamingChatLanguageModel streamingChatLanguageModel;
//
//
//    @GetMapping("/chatStream")
//    public Flux<String> modelStream(@RequestParam(value = "message", defaultValue = "Hello") String message) {
//        StreamingChatResponseHandler streamingChatResponseHandler = new StreamingChatResponseHandler();
//        return streamingChatLanguageModel.chat(message, );
//    }
}
