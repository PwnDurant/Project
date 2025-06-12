package com.zqq.cookieshop.controller.goods;

import com.zqq.cookieshop.service.IGoodsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("goods")
public class GoodsController {

    @Resource(name = "goodsServiceImpl")
    private IGoodsService iGoodsService;



}
