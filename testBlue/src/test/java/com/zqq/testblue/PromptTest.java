package com.zqq.testblue;


import com.zqq.testblue.assistant.SeparateAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PromptTest {

    @Autowired
    private SeparateAssistant separateAssistant;

    @Test
    public void testSystemMessage(){
        System.out.println(separateAssistant.chat(3,"今天几号"));
    }

    @Test
    public void testSystemMessage1(){
        System.out.println(separateAssistant.chat2(3,"今天几号"));
    }

    @Test
    public void testSystemMessage2(){
        System.out.println(separateAssistant.chat3(5,"我是谁，我多大了","翠花",18));
    }

}
