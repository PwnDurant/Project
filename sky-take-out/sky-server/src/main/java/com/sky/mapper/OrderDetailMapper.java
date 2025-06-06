package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {


    /**
     * 批量插入一个订单关联的订单详情
     * @param orderDetailsList
     */
    void insertBatch(List<OrderDetail> orderDetailsList);
}
