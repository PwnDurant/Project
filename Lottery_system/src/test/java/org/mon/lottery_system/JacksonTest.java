package org.mon.lottery_system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mon.lottery_system.common.pojo.CommonResult;
import org.mon.lottery_system.common.utils.JacksonUtil;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class JacksonTest {


    @Test
    void jacksonTest(){

        ObjectMapper objectMapper=new ObjectMapper();

        CommonResult<String> result=CommonResult.error(500,"系统错误");
        String str;

//        序列化。把一个对象序列化为一个String
        try {
            str= objectMapper.writeValueAsString(result);
            System.out.println(str);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


//        反序列化
        try {
            CommonResult<String> readResult=objectMapper.readValue(str,CommonResult.class);
            System.out.println(readResult);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


//        List序列化
        List<CommonResult<String>> commonResultList= Arrays.asList(
                CommonResult.success("success1"),
                CommonResult.success("success2")
        );
        try {
            str= objectMapper.writeValueAsString(commonResultList);
            System.out.println(str);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

//        List反序列化
//        先创建一个java类（构造）
        JavaType javaType=objectMapper.getTypeFactory().constructParametricType(List.class,CommonResult.class);

        try {
            objectMapper.readValue(str,javaType);
            for(CommonResult<String> commonResult:commonResultList){
                System.out.println(commonResult.getData());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void JacksonUtilTest(){
        CommonResult<String> result=CommonResult.success("success");
        String str;

//        序列化
        str= JacksonUtil.writeValueAsString(result);
        System.out.println(str);

//      反序列化
        result=JacksonUtil.readValue(str,CommonResult.class);
        System.out.println(result.getData());

        List<CommonResult<String>> commonResults=Arrays.asList(
                CommonResult.success("success1"),
                CommonResult.success("success")
        );

//        序列化List

        String s = JacksonUtil.writeValueAsString(commonResults);
        System.out.println(s);


//        反序列化List

        commonResults=JacksonUtil.readListValue(s,CommonResult.class);
        for(CommonResult<String> commonResult:commonResults){
            System.out.println(commonResult.getData());
        }
    }

}
