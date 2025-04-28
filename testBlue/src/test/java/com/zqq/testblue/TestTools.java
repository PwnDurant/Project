package com.zqq.testblue;

import com.zqq.testblue.assistant.SeparateAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTools {

    @Autowired
    private SeparateAssistant separateAssistant;

    @Test
    public void testCalcTools(){
        System.out.println(separateAssistant.chat(6, "3354+31451345等于几，58129的平方跟多少"));
    }

}
