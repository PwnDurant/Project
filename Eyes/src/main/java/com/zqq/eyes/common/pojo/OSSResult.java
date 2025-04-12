package com.zqq.eyes.common.domain;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OSSResult {

    private String name;

    private boolean success; //对象状态，true成功，false失败

}
