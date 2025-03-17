package com.zqq.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.system.JavaVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

@SpringBootTest
public class TestConditional {


    @Test
    void test(){
        System.out.println("执行test方法");
    }

}

@Slf4j
@Configuration
class AppConfig{
    @Bean
    @Conditional(Jdk17Conditional.class)
    public JDK17 jdk17(){
        System.out.println("jdk17");
        return new JDK17();
    }

    @Bean
    @Conditional(Jdk21Conditional.class)
    public JDK21 jdk21(){
        System.out.println("jdk21");
        return new JDK21();
    }
}

class JDK17{}
class JDK21{}

class Jdk17Conditional implements Condition{

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return JavaVersion.getJavaVersion().equals(JavaVersion.SEVENTEEN);
    }
}

class Jdk21Conditional implements Condition{

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return JavaVersion.getJavaVersion().equals(JavaVersion.TWENTY_ONE);
    }
}