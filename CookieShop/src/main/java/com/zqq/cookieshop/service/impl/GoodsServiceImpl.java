package com.zqq.cookieshop.service.impl;

import com.zqq.cookieshop.mapper.GoodsMapper;
import com.zqq.cookieshop.service.IGoodsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GoodsServiceImpl implements IGoodsService {

    private final GoodsMapper goodsMapper;

}
