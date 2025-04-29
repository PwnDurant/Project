package com.zqq.testchase.model.config;

import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "vivo.blue-lm") // 从 application.yml 读取配置
public class VivoBlueLMConfig {

    private String appId;

    private String appKey;

    private String domain = "api-ai.vivo.com.cn";

    private String uri = "/vivogpt/completions";

    private String model = "vivo-BlueLM-TB-Pro";

}