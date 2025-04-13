package com.zqq.user.domain.record.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecordVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long recordId;

    private String leftImage;    // 图像地址

    private String rightImage;    // 图像地址

    private String eyeType;      // LEFT / RIGHT / BOTH

    private String result;                // 识别结果

    private Float confidence;             // 置信度

    private String uploadTime;     // 上传时间

}
