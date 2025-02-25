package org.mon.lottery_system.common.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.JsonParseException;
import org.springframework.util.ReflectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.jar.JarException;

//单例管理
public class JacksonUtil {


    /**
     * 单例
     */
    private final static ObjectMapper OBJECT_MAPPER;

    /**
     * 在程序加载到时候执行，只执行一次，确保全局只有一个ObjectMapper对象
     */

    static {
        OBJECT_MAPPER=new ObjectMapper();
    }

    public static ObjectMapper getObjectMapper(){
        return OBJECT_MAPPER;
    }

    private static  <T> T tryParse(Callable<T> parser) {
        return tryParse(parser, JarException.class);
    }


    private static  <T> T tryParse(Callable<T> parser, Class<? extends Exception> check) {
        try {
            return parser.call();
        }
        catch (Exception ex) {
            if (check.isAssignableFrom(ex.getClass())) {
                throw new JsonParseException(ex);
            }
            ReflectionUtils.rethrowRuntimeException(ex);
            throw new IllegalStateException(ex);
        }
    }

//    序列化
    public static String writeValueAsString(Object object){
        return JacksonUtil.tryParse(()->{
            return JacksonUtil.getObjectMapper().writeValueAsString(object);
        });
    }

//    反序列化
    public static <T> T readValue(String content,Class<T> valueType){
        return JacksonUtil.tryParse(()->{
            return JacksonUtil.getObjectMapper().readValue(content,valueType);
        });
    }

//    反序列化List
    public static <T> T readListValue(String content,Class<?> paramClasses){

        JavaType javaType=JacksonUtil.getObjectMapper().getTypeFactory()
                .constructParametricType(List.class, paramClasses);

        return JacksonUtil.tryParse(()->{
            return JacksonUtil.getObjectMapper().readValue(content,javaType);
        });
    }

}
