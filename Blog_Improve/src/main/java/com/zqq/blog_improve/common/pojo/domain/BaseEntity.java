package com.zqq.blog_improve.common.pojo.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 公共类
 */
@Data
public class BaseEntity {

    /**
     * 删除标志位
     */
    private Integer deleteFlag;

    /**
     * 创建时间
     */
    private LocalDate createTime;

    /**
     * 更新时间
     */
    private LocalDate updateTime;

}
