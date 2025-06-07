package com.zqq.blog_improve.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 博客信息实体
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BlogInfo extends BaseEntity{

    /**
     * 设置自增组件
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 博客标题
     */
    private String title;

    /**
     * 博客内容
     */
    private String content;

    /**
     * 当前博客的用户Id
     */
    private Integer userId;

}
