package com.zqq.testblue.model;


import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.AiMessage;

import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

public class BlueLMChatModel implements ChatLanguageModel {

    private final WebClient webClient;
    private final String endpoint;
    private final String modelName;
    private final String apiKey;
    private final String appId;

    public BlueLMChatModel(BlueLMProperties properties) {
        this.endpoint = properties.getEndpoint();
        this.modelName = properties.getModelName();
        this.apiKey = properties.getApiKey();
        this.appId = properties.getAppId();
        this.webClient = WebClient.builder().build();
    }

    @Override
    public AiMessage generate(List<ChatMessage> messages) {
        ChatMessage lastMessage = messages.get(messages.size() - 1);
        String userMessage = lastMessage.toString();
        String result = sendRequest(userMessage);
        return AiMessage.from(result);
    }

    @Override
    public AiMessage doChat(List<ChatMessage> messages) {
        return generate(messages); // 直接调用 generate
    }

    private String sendRequest(String userMessage) {
        return webClient.post()
                .uri(endpoint)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue("""
                        {
                          "model": "%s",
                          "input": "%s",
                          "appId": "%s"
                        }
                        """.formatted(modelName, userMessage, appId))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}