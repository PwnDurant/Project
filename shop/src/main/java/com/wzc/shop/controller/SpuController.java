package com.thwh.shopmall.controller;

import com.thwh.shopmall.common.base.R;
import com.thwh.shopmall.domain.SkuInfo;
import com.thwh.shopmall.domain.SpuInfo;
import com.thwh.shopmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spu")
public class SpuController {
    
    @Autowired
    private SpuService spuService;
    
    @PostMapping("/addSpu")
    public R<Boolean> addSpu(@RequestBody SpuInfo SpuInfo){
        return spuService.addSpu(SpuInfo);
    }
    
    @GetMapping("/getSpuList")
    public R<List<SpuInfo>> getSpuList(){
        return spuService.getSpuList();
    }

    @GetMapping("/getDetail")
    public R<List<SkuInfo>> getDetail(Long spuId){
        return spuService.getDetail(spuId);
    }
    
    @PostMapping("/addSpuSku")
    public R<Boolean> addSpuSku(Long skuId,Long spuId){
        return spuService.addSpuSku(skuId,spuId);
    }
    
    @PostMapping("/reduceSpuSku")
    public R<Boolean> reduceSpuSku(Long skuId,Long spuId){
        return spuService.reduceSpuSku(skuId,spuId);
    }
    
    @PostMapping("/updateSpu")
    public R<Boolean> updateSpu(@RequestBody SpuInfo spuInfo){
        return spuService.updateSpu(spuInfo);
    }

    @PostMapping("/deleteSpu")
    public R<Boolean> deleteSpu(Long spuId){
        return spuService.deleteSpu(spuId);
    }
    
}
