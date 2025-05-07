package com.sky.annotation;


import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识某个方法需要进行功能字段自动填充
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {

//    数据库操作类型 ：UPDATE INSERT
//    这个注解要求你传一个值（类型是 OperationType），
//    用来告诉程序：“我现在是要插入还是更新”，方便注解背后的逻辑（比如 AOP）根据这个来自动填充字段。
    OperationType value();

}
