package com.zqq.testblue.model;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlueLMConfig {

    @Bean
    public BlueLMProperties blueLMProperties() {
        return new BlueLMProperties();
    }

    @Bean
    public ChatLanguageModel blueLMChatModel(BlueLMProperties properties) {
        return new BlueLMChatModel(properties);
    }
}