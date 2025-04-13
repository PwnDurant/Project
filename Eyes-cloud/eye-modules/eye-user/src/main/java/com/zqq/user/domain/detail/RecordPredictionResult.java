package com.zqq.user.domain.detail;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("record_prediction_result")
public class RecordPredictionResult {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "ID",type = IdType.ASSIGN_ID)
    private Long id;

    private Long recordId;

    private String diseaseName;

    private Double confidenceScore;

    @TableField(fill= FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill= FieldFill.UPDATE)
    private LocalDateTime updatedAt;
}
