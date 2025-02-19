package org.mon.library_management_system.Aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;



//AOP

@Slf4j
//@Component
@Aspect
public class AspectDemo1 {

    @Pointcut("execution(* org.mon.library_management_system.controller.*.*(..))")
    public void pt(){}

    @Before("pt()")
    public void doBefore(){
        log.info("demo1 执行doBefore...");
    }

    @After("pt()")
    public void doAfter(){
        log.info("demo1 执行doAfter...");
    }

    @AfterReturning("pt()")
    public void doAfterReturning(){
        log.info("demo1 执行doAfterReturning...");
    }

    @AfterThrowing("pt()")
    public void doAfterThrowing(){
        log.info("demo1 执行doAfterThrowing...");
    }

    @Around("pt()")
    public Object doAround(ProceedingJoinPoint jt){
        log.info("around 前处理");
        Object proceed=null;
        try{
            proceed=jt.proceed();
        } catch (Throwable e) {
            log.info("around发生异常...");
        }
        log.info("around后处理");
        return proceed;
    }
}
