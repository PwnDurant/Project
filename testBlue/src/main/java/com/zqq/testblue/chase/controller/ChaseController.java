package com.zqq.testblue.chase.controller;


import com.zqq.testblue.chase.assistant.ChaseAssistant;
import com.zqq.testblue.chase.domain.ChatForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Tag(name = "Chase 智能")
@RestController
@RequestMapping("/chase")
public class ChaseController {

    @Autowired
    private ChaseAssistant chaseAssistant;

//    @Operation(summary = "对话")
//    @PostMapping("/chat")
//    public String chat(@RequestBody ChatForm chatForm){
//        return chaseAssistant.chat(chatForm.getMemoryId(),chatForm.getMessage());
//    }

    @Operation(summary = "对话")
    @PostMapping(value = "/chat",produces = "text/stream;charset=utf-8")
    public Flux<String> chat(@RequestBody ChatForm chatForm){
        return chaseAssistant.chat(chatForm.getMemoryId(),chatForm.getMessage());
    }

}
