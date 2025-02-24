package org.mon.lottery_system.common.errorcode;

import lombok.Data;

//定义错误码
@Data
public class ErrorCode {
    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误描述
     */
    private final String message;

//   构造方法
    public ErrorCode(Integer code,String message){
        this.code=code;
        this.message=message;
    }

}
