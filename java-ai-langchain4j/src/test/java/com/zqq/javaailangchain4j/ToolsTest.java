package com.zqq.javaailangchain4j;

import com.zqq.javaailangchain4j.assistant.SeparateChatAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ToolsTest {
    @Autowired
    private SeparateChatAssistant separateChatAssistant;

    @Test
    public void testCalculatorTools(){
        String answer= separateChatAssistant.chat(2,"1+2等于几，456722348的平方跟是多少");
        System.out.println(answer);
    }


}
