package com.zqq.javaailangchain4j.model;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import okhttp3.*;
import com.google.gson.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VivoBlueLMChatModel implements ChatLanguageModel {

    private final String appId;
    private final String appSecret;
    private final String modelName;
    private final OkHttpClient client;
    private final Gson gson = new Gson();

    public VivoBlueLMChatModel(String appId, String appSecret, String modelName) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.modelName = modelName;
        this.client = new OkHttpClient();
    }


}