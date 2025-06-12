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

    /**
     * 下单的时候商品单价（防止价格变动影响历史订单）
     */
    private Float price;

    /**
     * 此商品在该订单中的购买数量
     */
    private Integer amount;

    /**
     * 商品 ID
     */
    private Long goodsId;

    /**
     * 所属订单 ID
     */
    private Long orderId;

}
