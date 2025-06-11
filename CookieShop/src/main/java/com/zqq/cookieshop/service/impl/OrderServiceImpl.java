package com.zqq.cookieshop.service.impl;

import com.zqq.cookieshop.mapper.OrderMapper;
import com.zqq.cookieshop.service.IOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderMapper orderMapper;

}
