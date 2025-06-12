package com.zqq.cookieshop.domain;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


/**
 * 订单实体类
 */
@Data
public class Order {

    /**
     * 订单ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 当前订单对应的用户Id
     */
    private Long userId;

    /**
     * 商品总价
     */
    private Float total;

    /**
     * 商品总件数
     */
    private Integer amount;

    /**
     * 当前订单状态
     */
    private Integer status;

    /**
     * 支付方式（微信/支付宝/银行卡）
     */
    private Integer payType;

    /**
     * 收获人姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 下单时间
     */
    private DateTime dateTime;

}
