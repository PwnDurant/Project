package com.zqq.user.domain.eye.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EyeVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private String name;            // 疾病名称

    private String description;     // 疾病描述

    private String nickName;        //标签名称

    private String recoveryAdvice;  // 康复建议

    private String severityLevel;   // 危险程度（低/中/高）

    private String causes;          // 常见诱因

    private String symptoms;        // 症状描述

    private String treatment;       // 治疗方式

    private Boolean isContagious;   // 是否传染

    private String imageUrl;        // 图片链接

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;        //更新时间

}
