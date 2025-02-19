package org.mon.library_management_system.Aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimeRecordAspect {
    @Around("org.mon.library_management_system.Aspect.AspectDemo1.pt()")
    public Object timeRecord(ProceedingJoinPoint pjt) throws Throwable {
//        1，记录开始时间
//        2，执行目标代码
//        3，记录结束时间
//        4，返回结果
        log.info("TimeRecorsAspect 前");
        long start =System.currentTimeMillis();

//        执行目标方法
        Object o=null;
        o=pjt.proceed();
        log.info("TimeRecordAspect 后");
        log.info(pjt.getSignature()+"耗时:"+(System.currentTimeMillis()-start)+"ms");
        return o;
    }
}
