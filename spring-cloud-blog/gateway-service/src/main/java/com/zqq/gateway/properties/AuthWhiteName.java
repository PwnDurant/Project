package com.zqq.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "auth.white")
public class AuthWhiteName {

    private List<String> url;

}
