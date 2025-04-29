package com.zqq.testchase.model.service.impl;

import com.zqq.testchase.model.auth.VivoAuth;
import com.zqq.testchase.model.config.VivoBlueLMConfig;
import com.zqq.testchase.model.domain.ResponseData;
import com.zqq.testchase.model.service.IBlueService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

@Service
public class VivoBlueLMService implements IBlueService {

    private final VivoBlueLMConfig config;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // 通过构造函数注入依赖
    public VivoBlueLMService(VivoBlueLMConfig config, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.config = config;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public String chat(String prompt, String systemPrompt) {
        try {
            // 构建请求参数
            String requestId = UUID.randomUUID().toString();
            String sessionId = UUID.randomUUID().toString();

            // 构造请求体
            Map<String, Object> body = new HashMap<>();
            body.put("prompt", prompt);
            body.put("model", config.getModel());
            body.put("sessionId", sessionId);
            body.put("systemPrompt", systemPrompt);

            // 构造URL参数
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("requestId", requestId);
            String queryString = mapToQueryString(queryParams);

            // 构造完整URL
            String url = String.format("https://%s%s?%s", config.getDomain(), config.getUri(), queryString);

            // 生成请求头
            HttpHeaders headers = VivoAuth.generateAuthHeaders(
                    config.getAppId(),
                    config.getAppKey(),
                    "POST",
                    config.getUri(),
                    queryString
            );
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 发送请求
            HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            // 处理响应
            if (response.getStatusCode() == HttpStatus.OK) {
                ResponseData responseData = objectMapper.readValue(response.getBody(), ResponseData.class);
                return responseData.getData().getContent();
//                return response.getBody();
            } else {
                throw new RuntimeException("API调用失败，状态码：" + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("请求处理异常", e);
        }
    }

    private String mapToQueryString(Map<String, Object> map) {
        if (map.isEmpty()) {
            return "";
        }
        StringBuilder queryStringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (queryStringBuilder.length() > 0) {
                queryStringBuilder.append("&");
            }
            queryStringBuilder.append(entry.getKey());
            queryStringBuilder.append("=");
            queryStringBuilder.append(entry.getValue());
        }
        return queryStringBuilder.toString();
    }
}