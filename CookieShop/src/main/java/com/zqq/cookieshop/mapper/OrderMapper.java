package com.zqq.cookieshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zqq.cookieshop.domain.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
