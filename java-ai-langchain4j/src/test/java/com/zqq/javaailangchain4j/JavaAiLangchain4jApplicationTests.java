package com.zqq.javaailangchain4j;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;

@SpringBootTest
class JavaAiLangchain4jApplicationTests {

//    @Autowired
//    private OpenAiChatModel openAiChatModel;

//    @Autowired
//    private ChatLanguageModel chat;

    @Test
    void contextLoads() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();
        String answer = model.chat("你是谁");
        System.out.println(answer);
    }

//    @Test
//    void testSpringBoot(){
//        String answer = chat.chat("我是谁");
//        System.out.println(answer);
//    }

    @Test
    void test(){
        System.out.println(System.getenv("API_KEY"));
    }


    @Autowired
    private QwenChatModel qwenChatModel;
    @Test
    public void testDashScopeQwen() {
//向模型提问
        String answer = qwenChatModel.chat("你好");
//输出结果
        System.out.println(answer);
    }

    @Test
    public void testPic(){
        WanxImageModel wanxImageModel= WanxImageModel.builder()
                .modelName("wanx2.1-t2i-plus")
                .apiKey("sk-baaabbe8fa984200af5da2740fcabbab")
                .build();
        Response<Image> generate = wanxImageModel.generate("奇幻森林精灵：在一片弥漫着轻柔薄雾的\n" +
                "古老森林深处，阳光透过茂密枝叶洒下金色光斑。\n" +
                "一位身材娇小、长着透明薄翼的精灵少女站在一朵硕大的蘑菇上。她\n" +
                "有着海藻般的绿色长发，发间点缀着蓝色的小花，皮肤泛着珍珠般的微光。身上穿着由翠绿树叶和白色藤蔓编织而成的\n" +
                "连衣裙，手中捧着一颗散发着柔和光芒的水晶球，周围环绕着五彩斑斓的蝴蝶，脚下是铺满苔藓的地面，蘑菇和蕨类植\n" +
                "物丛生，营造出神秘而梦幻的氛围。");
        URI url = generate.content().url();
        System.out.println(url);
    }

}
