package com.zqq.cookieshop.domain;

import lombok.Data;

/**
 * 推荐实体类
 */
@Data
public class Recommend {

    /**
     * 推荐表的 ID
     */
    private Long id;

    /**
     * 推荐类型，推荐类型
     */
    private Integer type;

    /**
     * 关联的商品 ID
     */
    private Long goodsId;

}
