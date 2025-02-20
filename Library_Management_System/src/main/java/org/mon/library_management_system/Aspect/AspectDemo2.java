package org.mon.library_management_system.Aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
//@Component
@Order(3)
public class AspectDemo2 {

    @Pointcut("execution(* org.mon.library_management_system.controller.*.*(..))")
    public void pt(){}

    @Before("pt()")
    public void doBefore(){
        log.info("demo2 执行doBefore...");
    }

    @After("pt()")
    public void doAfter(){
        log.info("demo2 执行doAfter...");
    }
}
