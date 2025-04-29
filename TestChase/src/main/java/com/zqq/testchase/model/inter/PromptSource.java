package com.zqq.testchase.model.inter;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PromptSource {
    String value(); // 文件路径，如 "classpath:prompts/poet.txt"
}