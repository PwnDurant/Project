package com.zqq.testchase.model.controller;

import com.zqq.testchase.model.inter.PromptSource;
import com.zqq.testchase.model.service.impl.VivoBlueLMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private VivoBlueLMService blueLMService;

    @GetMapping
    @PromptSource("classpath:poet-system-prompt.txt")
    public String chat(String systemPrompt) {
//        String systemPrompt = "你是一个诗人助手，用七言绝句风格回答问题。";
        return blueLMService.chat("你是谁", systemPrompt);
    }

}