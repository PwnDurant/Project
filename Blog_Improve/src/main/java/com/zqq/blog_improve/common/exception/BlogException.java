package com.zqq.blog_improve.common.exception;

import com.zqq.blog_improve.common.base.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class BlogException extends RuntimeException{

    /**
     * 发送异常抛出的状态码
     */
    private ResultCode resultCode;

}
