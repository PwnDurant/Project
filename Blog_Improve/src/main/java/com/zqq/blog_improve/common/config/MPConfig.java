package com.zqq.blog_improve.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zqq.blog_improve.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;


/**
 * 虽然数据库设置 CURRENT_TIMESTAMP 非常方便，但存在这些局限：
 * 	1.	 不能同时为多个字段使用 ON UPDATE CURRENT_TIMESTAMP；
 * 	2.	 不好配合 ORM 使用（如 MyBatis-Plus 不知道数据库值是怎么来的）；
 * 	3.	 改表结构或数据库迁移时可能出错（跨数据库兼容性差）；
 * 	4.	 无法填充如 updateBy、createBy 等业务相关字段。
 */
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
