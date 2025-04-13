package com.zqq.user.domain.result;

import lombok.Data;

@Data
public class RecordResult {

    private String result;                // 识别结果

    private Float confidence;             // 置信度

}
