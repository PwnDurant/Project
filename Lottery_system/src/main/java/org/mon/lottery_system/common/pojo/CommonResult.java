package org.mon.lottery_system.common.pojo;

import lombok.Data;
import org.mon.lottery_system.common.errorcode.ErrorCode;
import org.mon.lottery_system.common.errorcode.GlobalErrorCodeConstants;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * 统一数据返回
 * @param <T>
 */

@Data
public class CommonResult<T> implements Serializable {

//    返回的错误码
    private Integer code;
//    正常返回的数据
    private T data;
//    错误码描述
    private String msg;

    public  static <T> CommonResult<T> success(T data){
        CommonResult<T> result=new CommonResult<>();
        result.code= GlobalErrorCodeConstants.SUCCESS.getCode();
        result.data=data;
        result.msg="";
        return result;
    }

    public static <T> CommonResult<T> error(Integer code,String msg){
//        先断言
        Assert.isTrue(GlobalErrorCodeConstants.SUCCESS.getCode().equals(code),"code 不是错误的异常");
        CommonResult<T> result=new CommonResult<>();
        result.code=code;
        result.msg=msg;
        return result;
    }

    public static <T> CommonResult<T> error(ErrorCode errorCode){
        return error(errorCode.getCode(),errorCode.getMessage());
    }

}
