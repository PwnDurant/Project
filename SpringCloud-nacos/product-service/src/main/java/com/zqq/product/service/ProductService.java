package com.zqq.product.service;

import com.zqq.product.mapper.ProductMapper;
import com.zqq.product.model.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class ProductService implements ProductMapper{

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductInfo selectProductById(Integer productId) {
        log.info("接收到请求");
        return productMapper.selectProductById(productId);
    }
}
