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
     *
     */
    private Float total;

    /**
     *
     */
    private Integer amount;

    /**
     * 当前订单状态
     */
    private Integer status;

    /**
     *
     */
    private Integer payType;

    /**
     *
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
