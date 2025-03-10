package org.mon.spring_principles.controller;


import jakarta.annotation.Resource;
import org.mon.spring_principles.model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scope")
public class ScopeController {

    @Resource(name = "singleDog")
    private Dog singleDog;

    @Resource(name = "prototypeDog")
    private Dog prototypeDog;

    @Resource(name = "requestDog")
    private Dog requestDog;

    @Resource(name = "sessionDog")
    private Dog sessionDog;

    @Resource(name = "applicationDog")
    private Dog applicationDog;

    @Autowired
    private ApplicationContext context;

    @RequestMapping("/single")
    public String single(){
//        从context中获取对象
        Dog dog=(Dog)context.getBean("singleDog");
        return "注入对象:"+singleDog+",从context中："+dog;
//        注入对象:org.mon.spring_principles.model.Dog@4aa34a76,从context中：org.mon.spring_principles.model.Dog@4aa34a76
//        多次请求获得的是同一个对象
    }

    @RequestMapping("/prototype")
    public String prototype(){
//        从context中获取对象
        Dog dog=(Dog)context.getBean("prototypeDog");
        return "注入对象:"+prototypeDog+",从context中："+dog;
//        注入对象:org.mon.spring_principles.model.Dog@4444ecda,从context中：org.mon.spring_principles.model.Dog@1467a36
//        多次请求的context每次的对象都会发生变化，而注入的对象，在我们程序启动的时候就已经注入进来了，不会再发生变化
    }

    @RequestMapping("/request")
    public String request(){
        Dog dog=(Dog) context.getBean("requestDog");
        return "注入对象:"+requestDog+",从context中："+dog;
//        注入对象:org.mon.spring_principles.model.Dog@190d02d0,从context中：org.mon.spring_principles.model.Dog@190d02d0
//        在同一个request中是同一个对象，在不同的请求中就不是同一个对象
    }

    @RequestMapping("/session")
    public String session(){
        Dog dog=(Dog) context.getBean("sessionDog");
        return "注入对象:"+sessionDog+",从context中："+dog;
//        注入对象:org.mon.spring_principles.model.Dog@181104b,从context中：org.mon.spring_principles.model.Dog@181104b
//        在同一个session中获取的对象是同一个对象，多个用户同时访问的时候就不需要处理，因为获取的不是同一个session
        }

    @RequestMapping("/application")
    public String application(){
        Dog dog=(Dog) context.getBean("applicationDog");
        return "注入对象:"+applicationDog+",从context中："+dog;
//        注入对象:org.mon.spring_principles.model.Dog@3ddd28b1,从context中：org.mon.spring_principles.model.Dog@3ddd28b1
//        不同的会话（不同的客户端），得到的依然是同一个对象，因为他们是在同一个application中
    }

//    一个Web容器中application context可以有多个，就是可以有多个进程
//    当前：一个tomcat可以部署多个服务
//    实际：一个tomcat可以部署多个服务
//    而application scope是对于整个web容器而言的

}
