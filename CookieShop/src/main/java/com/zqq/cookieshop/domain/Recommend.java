package com.zqq.cookieshop.domain;

import lombok.Data;

/**
 * 推荐实体类
 */
@Data
public class Recommend {

    private Long id;

    private Integer type;

    private Long goodsId;

}
