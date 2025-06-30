package com.thwh.shopmall.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thwh.shopmall.common.base.R;
import com.thwh.shopmall.common.base.ResultCode;
import com.thwh.shopmall.common.exception.SystemException;
import com.thwh.shopmall.domain.SkuInfo;
import com.thwh.shopmall.mapper.SkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuService {
    
    @Autowired
    private SkuMapper skuMapper;

    public R<List<SkuInfo>> getSkuList() {
        List<SkuInfo> SkuInfos = skuMapper.selectList(new LambdaQueryWrapper<SkuInfo>()
                .eq(SkuInfo::getDeleteFlag, 0)
                .orderByDesc(SkuInfo::getCreateTime));
        return R.ok(SkuInfos);
    }

    public R<Boolean> addSku(SkuInfo skuInfo) {
        SkuInfo SkuInfo1 = skuMapper.selectOne(new LambdaQueryWrapper<SkuInfo>()
                .eq(SkuInfo::getSkuName, skuInfo.getSkuName())
                .eq(SkuInfo::getDeleteFlag, 0));
        if(SkuInfo1!=null) throw new SystemException(ResultCode.FAILED);
        int insert = skuMapper.insert(skuInfo);
        if(insert==1) return R.ok();
        return R.fail();
    }

    public R<Boolean> updateSku(SkuInfo skuInfo) {
        SkuInfo SkuInfo1 = skuMapper.selectOne(new LambdaQueryWrapper<SkuInfo>()
                .eq(SkuInfo::getId, skuInfo.getId())
                .eq(SkuInfo::getDeleteFlag, 0));
        if(SkuInfo1==null) throw new SystemException(ResultCode.FAILED);
        int row = skuMapper.updateById(skuInfo);
        if(row==1) return R.ok();
        return R.fail();
    }

    public R<Boolean> deleteSku(Long skuId) {
        SkuInfo skuInfo1 = skuMapper.selectOne(new LambdaQueryWrapper<SkuInfo>()
                .eq(SkuInfo::getId, skuId)
                .eq(SkuInfo::getDeleteFlag, 0));
        if(skuInfo1==null) throw new SystemException(ResultCode.FAILED);
        skuInfo1.setDeleteFlag(1);
        int row = skuMapper.updateById(skuInfo1);
        if(row==1) return R.ok();
        return R.fail();
    }
    
}
