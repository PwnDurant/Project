package com.zqq.cookieshop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 订单商品实体类
 */
@Data
public class OrderItem {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private Float price;

    private Integer amount;

    private Long goodsId;

    private Long orderId;

}
