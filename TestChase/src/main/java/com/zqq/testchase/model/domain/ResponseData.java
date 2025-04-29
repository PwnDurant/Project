package com.zqq.testchase.model.domain;

import com.zqq.testchase.model.domain.innerInfo.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {

    private int code;

    private Data data;

    private String msg;


}
