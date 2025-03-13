package com.zqq.eyes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EyesApplicationTests {

    @Test
    void contextLoads() {
        String test="asgasf";
        System.out.println(test.substring(test.lastIndexOf("g")));
    }

}
