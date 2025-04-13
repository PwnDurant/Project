package com.zqq.user.domain.eye;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("eye_disease")
public class Eye {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "ID",type = IdType.ASSIGN_ID)
    private Long id;                 // 主键ID

    private String name;            // 疾病名称

    private String description;     // 疾病描述

    private String recoveryAdvice;  // 康复建议

    private String severityLevel;   // 危险程度（低/中/高）

    private String causes;          // 常见诱因

    private String symptoms;        // 症状描述

    private String treatment;       // 治疗方式

    private Boolean isContagious;   // 是否传染

    private String imageUrl;        // 图片链接

    @TableField(fill= FieldFill.INSERT)
    private LocalDateTime createAt;         // 创建时间

    @TableField(fill= FieldFill.UPDATE)
    private LocalDateTime updatedAt;         // 更新时间
}