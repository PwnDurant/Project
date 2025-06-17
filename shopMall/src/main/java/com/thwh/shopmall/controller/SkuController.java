package com.thwh.shopmall.controller;

import com.thwh.shopmall.common.base.R;
import com.thwh.shopmall.domain.SkuInfo;
import com.thwh.shopmall.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sku")
public class SkuController {
    
    @Autowired
    private SkuService skuService;

    @RequestMapping("/getSkuList")
    public R<List<SkuInfo>> getSkuList(){
        return skuService.getSkuList();
    }

    @RequestMapping("/addSku")
    public R<Boolean> addSku(@RequestBody SkuInfo skuInfo){
        return skuService.addSku(skuInfo);
    }

    @RequestMapping("/updateSku")
    public R<Boolean> updateSku(@RequestBody SkuInfo skuInfo){
        return skuService.updateSku(skuInfo);
    }

    @RequestMapping("/deleteSku")
    public R<Boolean> deleteSku(Long skuId){
        return skuService.deleteSku(skuId);
    }
    
}
