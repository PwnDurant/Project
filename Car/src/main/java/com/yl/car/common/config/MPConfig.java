package com.wpf.roomsimple.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Slf4j
@Configuration
public class MPConfig implements MetaObjectHandler {


    /**
     * 用于插入时自动填充
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill...");
        this.strictInsertFill(metaObject,"createTime", LocalDateTime.class,LocalDateTime.now());  //仅当该字段为 null 的时候填充
//        this.strictInsertFill(metaObject,"createBy", Long.class,ThreadLocalUtil.get(Constants.USER_ID,Long.class));

        this.strictInsertFill(metaObject,"updateTime", LocalDateTime.class,LocalDateTime.now());  //仅当该字段为 null 的时候填充
//        this.strictInsertFill(metaObject,"updateBy", Long.class,ThreadLocalUtil.get(Constants.USER_ID,Long.class));
    }

    /**
     * 用于更新时自动填充
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill...");
        this.setFieldValByName("updateTime",LocalDateTime.now(),metaObject);  //强制设置字段值，不管之前有没有值
//        this.setFieldValByName("updateBy",ThreadLocalUtil.get(Constants.USER_ID,Long.class));

    }
}
