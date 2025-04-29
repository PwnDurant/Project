package com.zqq.testchase.model.domain.innerInfo;

@lombok.Data
public class Data {

    private String content;

    private String sessionId;

    private String requestId;

    private String reasoningContent;

    private String image;

    private String functionCall;

    private String toolCall;

    private String toolCalls;

    private String contentList;

    private String searchInfo;

    private Usage usage;

    private String provider;

    private String clearHistory;

    private String searchExtra;

    private String model;

    private String finishReason;

    private String score;

    private ModelInfo modelInfo;


}
