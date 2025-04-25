package com.zqq.javaailangchain4j.controller;


import com.zqq.javaailangchain4j.assistant.ChaseAgent;
import com.zqq.javaailangchain4j.bean.ChatForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Tag(name="Chase")
@RequestMapping("/chase")
public class ChaseController {

    @Autowired
    private ChaseAgent chaseAgent;

    @Operation(summary = "对话")
    @PostMapping(value = "/chat",produces = "text/stream;charset=utf-8")
    public Flux<String> chat(@RequestBody ChatForm chatForm){
        return chaseAgent.chat(chatForm.getMemoryId(),chatForm.getMessage());
    }

}
