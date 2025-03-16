package com.zqq.order.model;

import lombok.Data;

import java.util.Date;

@Data
public class ProductInfo {

    private Integer id;
    private String productName;
    private Integer price;
    private Integer state;
    private Date createTime;
    private Date updateTime;

}
