package com.zqq.testchase.model.aspect;

import com.zqq.testchase.model.inter.PromptSource;
import com.zqq.testchase.model.tools.PromptLoader;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PromptSourceAspect {
    private final PromptLoader promptLoader;

    public PromptSourceAspect(PromptLoader promptLoader) {
        this.promptLoader = promptLoader;
    }

    @Around("@annotation(promptSource)")
    public Object injectPrompt(ProceedingJoinPoint joinPoint, PromptSource promptSource) throws Throwable {
        // 加载提示词
        String prompt = promptLoader.loadPrompt(promptSource.value());
        
        // 修改方法参数（假设systemPrompt是方法的第一个参数）
        Object[] args = joinPoint.getArgs();
        args[0] = prompt; // 根据实际参数位置调整
        
        return joinPoint.proceed(args);
    }
}