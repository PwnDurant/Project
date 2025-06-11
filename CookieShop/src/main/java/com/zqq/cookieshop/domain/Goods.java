package com.zqq.cookieshop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品实体类
 */
@Data
public class Goods {

    /**
     * 商品ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 商品类型ID
     */
    private Long typeId;

    /**
     * 商品姓名
     */
    private String name;

    /**
     * 商品封面
     */
    private String cover;

    /**
     * 商品图片1
     */
    private String image1;

    /**
     * 商品图片2
     */
    private String image2;

    /**
     * 商品价格
     */
    private Float price;

    /**
     * 商品介绍
     */
    private String intro;

    /**
     * 商品库存
     */
    private Integer stock;

}
