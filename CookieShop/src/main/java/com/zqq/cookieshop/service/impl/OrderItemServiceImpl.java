package com.zqq.cookieshop.service.impl;

import com.zqq.cookieshop.mapper.OrderMapper;
import com.zqq.cookieshop.service.IOrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements IOrderItemService {

    private final OrderMapper orderMapper;

}
