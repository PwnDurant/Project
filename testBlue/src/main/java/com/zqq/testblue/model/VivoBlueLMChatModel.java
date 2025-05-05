package com.zqq.testblue.model;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Component
public class VivoBlueLMChatModel implements ChatLanguageModel {

    private static final String API_URL = "https://api-ai.vivo.com.cn/vivogpt/completions";
    private static final String MODEL_NAME = "vivo-BlueLM-TB-Pro";

    private final String appId="2025498193";
    private final String appKey="TFIgXzVvuKbKXTFP";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public VivoBlueLMChatModel() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ChatResponse doChat(ChatRequest chatRequest) {
        try {
            String requestId = UUID.randomUUID().toString();
            String sessionId = UUID.randomUUID().toString();

            // 组装请求体
            Map<String, Object> body = new HashMap<>();
            body.put("model", MODEL_NAME);
            body.put("messages", convertMessages(chatRequest.messages()));
            body.put("sessionId", sessionId);

            String urlParams = "requestId=" + requestId;

            // 生成签名
            HttpHeaders authHeaders = VivoAuth.generateAuthHeaders(appId, appKey, "POST", "/vivogpt/completions", urlParams);
            authHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, authHeaders);

            // 发送请求
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_URL + "?requestId=" + URLEncoder.encode(requestId, "UTF-8"),
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            // 解析返回
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && (Integer) responseBody.get("code") == 0) {
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                String content = (String) data.get("content");
                AiMessage aiMessage = new AiMessage(content);
                return ChatResponse.builder().aiMessage(aiMessage).build();
            } else {
                throw new RuntimeException("Vivo API Error: " + responseBody.getOrDefault("msg", "未知错误"));
            }

        } catch (Exception e) {
            throw new RuntimeException("调用Vivo BlueLM出错：" + e.getMessage(), e);
        }
    }

    @Override
    public ModelProvider provider() {
        return ModelProvider.OTHER;
    }

    private List<Map<String, String>> convertMessages(List<ChatMessage> messages) {
        List<Map<String, String>> list = new ArrayList<>();
        for (ChatMessage message : messages) {
            Map<String, String> map = new HashMap<>();
            switch (message.type()) {
                case USER:
                    map.put("role", "user");
                    map.put("content", message.toString());
                    break;
                case AI:
                    map.put("role", "assistant");
                    map.put("content", message.toString());
                    break;
                case SYSTEM:
                    map.put("role", "system");
                    map.put("content", message.toString());
                    break;
                default:
                    // vivo接口不支持function_call、tool_call等
                    throw new IllegalArgumentException("Unsupported message type: " + message.type());
            }
            list.add(map);
        }
        return list;
    }
}