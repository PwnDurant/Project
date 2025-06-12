package com.zqq.cookieshop.controller.order;

import com.zqq.cookieshop.service.IOrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Resource(name = "orderServiceImpl")
    private IOrderService orderService;



}
