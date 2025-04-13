package com.zqq.common.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zqq.common.core.constants.Constants;
import com.zqq.common.core.utils.ThreadLocalIUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 设置自动更新创建人，创建时间，更新人，更新时间
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject,"createTime", LocalDateTime.class,LocalDateTime.now());
        this.strictInsertFill(metaObject,"uploadTime", LocalDateTime.class,LocalDateTime.now());
        this.strictInsertFill(metaObject,"createAt", LocalDateTime.class,LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateAt", LocalDateTime.now(), metaObject);
    }
}
