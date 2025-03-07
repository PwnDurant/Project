package org.mon.gobang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GoBangApplication {

    public static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context=SpringApplication.run(GoBangApplication.class, args);
    }

}
