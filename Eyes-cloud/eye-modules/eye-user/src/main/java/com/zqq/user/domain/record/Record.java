package com.zqq.user.domain.record;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("eye_record")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Record {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "RECORD_ID",type = IdType.ASSIGN_ID)
    private Long recordId;       // 记录ID

    private Long userId;         // 用户ID（外键）

    private String leftImage;    // 左边图像地址

    private String rightImage;    // 右边图像地址

    private String eyeType;      // LEFT / RIGHT / BOTH

    @TableField(fill= FieldFill.INSERT)
    private LocalDateTime uploadTime;     // 上传时间

    private String result;                // 识别结果

    private Float confidence;             // 置信度

    private String recommendation;        // AI建议

    private LocalDateTime reviewTime;     // 医生复审时间

    private String reviewNotes;           // 医生备注

}
