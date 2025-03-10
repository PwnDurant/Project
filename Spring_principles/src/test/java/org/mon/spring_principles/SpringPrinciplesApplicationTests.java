package org.mon.spring_principles;

import org.junit.jupiter.api.Test;
import org.mon.spring_principles.component.BeanLifeComponent;
import org.mon.spring_principles.model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class SpringPrinciplesApplicationTests {


//    通过注解的方式进行拿去，直接注入进来
    @Autowired
    private ApplicationContext context;  //这个就是Spring容器


    @Test
    void contextLoads() {
//        两个拿取的Bean都是同一个Bean，打印的对象是相同的

//        Spring管理的Bean是单例模式，不是线程安全的，两个人可能会同时读取一个对象
//        Bean的作用域有6种，这是官方的说明

//        单例作用域  在application容器中获取的都是同一个对象
//        Dog bean = context.getBean(Dog.class);
//        System.out.println(bean.getName());
//        bean.setName("设置后的名称");
//
//        Dog bean2 = context.getBean(Dog.class);
//        System.out.println(bean2.getName());


//        多例作用域prototype，   只能在web环境中：request，session，application，websocket
    }

//    @Test
//    void test(){
//        BeanLifeComponent bean = context.getBean(BeanLifeComponent.class);
//        bean.use();
//    }


}
