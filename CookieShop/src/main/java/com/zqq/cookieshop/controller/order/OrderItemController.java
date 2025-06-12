package com.zqq.cookieshop.controller.order;

import com.zqq.cookieshop.service.IOrderItemService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderItem")
@Slf4j
public class OrderItemController {

    @Resource(name = "orderItemServiceImpl")
    private IOrderItemService orderItemService;

}
