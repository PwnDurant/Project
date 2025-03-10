package org.mon.spring_principles.config;

//学习Spring如何帮我们管理Bean
//1，类注解
//2，方法注解 @Bean

import org.mon.spring_principles.model.Dog;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class DogConfig {
//    通过这个注解帮我们管理了这个Bean，这个注解返回的是方法返回的对象，而不是方法本身
    @Bean
    public Dog dog(){
        Dog dog=new Dog();
        dog.setName("test");
        return dog;
    }


    @Bean
//    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Scope("singleton")  //默认就是单例作用域
    public Dog singleDog(){
        return new Dog();
    }

    @Bean
//    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Scope("prototype")  //多例作用域
    public Dog prototypeDog(){
        return new Dog();
    }

    @Bean
    @RequestScope //声明是一个request模式
    public Dog requestDog(){
        return new Dog();
    }

    @Bean
    @SessionScope //声明是一个request模式
    public Dog sessionDog(){
        return new Dog();
    }

    @Bean
    @ApplicationScope //声明是一个request模式
    public Dog applicationDog(){
        return new Dog();
    }

}
