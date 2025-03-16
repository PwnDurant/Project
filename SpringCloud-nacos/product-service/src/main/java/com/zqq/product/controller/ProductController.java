package com.zqq.product.controller;

import com.zqq.product.model.ProductInfo;
import com.zqq.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/product")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("{productId}")
    public ProductInfo getProductById(@PathVariable("productId") Integer productId){
        return productService.selectProductById(productId);
    }

}
