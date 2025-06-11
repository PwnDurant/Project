package com.zqq.cookieshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zqq.cookieshop.domain.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
