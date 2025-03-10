package org.mon.spring_principles;


import autoConfig.EnableTestConfig;
import autoConfig.MyImport;
import autoConfig.testConfig;
import autoConfig.testConfig1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;


//@SpringBootApplication(scanBasePackageClasses = testConfig.class)
@SpringBootApplication
//@Import(testConfig.class)
//@EnableTestConfig
//@Import(MyImport.class)
public class SpringPrinciplesApplication {

    public static void main(String[] args) {
//        这是原来拿去Bean的方式
        ApplicationContext context = SpringApplication.run(SpringPrinciplesApplication.class, args);
        testConfig bean = context.getBean(testConfig.class);
        bean.use();

        testConfig1 bean1 = context.getBean(testConfig1.class);
        bean1.use();

    }

}
