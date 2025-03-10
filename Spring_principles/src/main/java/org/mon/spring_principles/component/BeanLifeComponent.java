package org.mon.spring_principles.component;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import org.mon.spring_principles.model.Dog;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanLifeComponent implements BeanNameAware {
//    属性注入
    private Dog dog;

    public BeanLifeComponent(){
        System.out.println("执行构造函数...");
    }


//    让Spring自动给Dog bean给注入进来
    @Autowired
    public void setDog(Dog dog){
        this.dog=dog;
        System.out.println("属性注入...，使用set方法完成属性装配...");
    }

//    作用是在Bean完成依赖注入之后自动执行某一个方法，比构造函数和Aut之后执行
    @PostConstruct
    public void init(){
        System.out.println("执行init方法...");
    }


//  执行通知，比如BeanName之类的
    @Override
    public void setBeanName(String name) {
        System.out.println("执行通知setBeanName....."+name);
    }

//    使用Bean
    public void use(){
        System.out.println("执行use方法...");
    }


//    销毁之前执行的方法
    @PreDestroy
    public void destory(){
        System.out.println("执行destory方法....");
    }


}
