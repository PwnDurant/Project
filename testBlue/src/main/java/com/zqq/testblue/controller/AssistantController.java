package com.zqq.testblue.controller;

import com.zqq.testblue.assistant.AssistantQwen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssistantController {

    @Autowired
    AssistantQwen assistantQwen;

    @GetMapping("/chatAssistant")
    public String chat(String message) {
        return assistantQwen.chat(message);
    }
}
