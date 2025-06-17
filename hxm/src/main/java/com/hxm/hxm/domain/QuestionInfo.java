package com.thwh.shopmall.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SkuInfo extends BaseEntity{

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String skuName;

    private Long spuId;

}
