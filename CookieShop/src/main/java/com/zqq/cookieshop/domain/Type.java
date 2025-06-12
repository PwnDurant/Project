package com.zqq.cookieshop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 种类实体类
 */
@Data
public class Type {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 商品类型名称
     */
    private String name;

}
