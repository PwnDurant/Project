package com.thwh.shopmall.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thwh.shopmall.common.base.R;
import com.thwh.shopmall.common.base.ResultCode;
import com.thwh.shopmall.common.exception.SystemException;
import com.thwh.shopmall.domain.SkuInfo;
import com.thwh.shopmall.domain.SpuInfo;
import com.thwh.shopmall.mapper.SkuMapper;
import com.thwh.shopmall.mapper.SpuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpuService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuMapper skuMapper;

    public R<Boolean> addSpu(SpuInfo spuInfo) {
        SpuInfo spuInfo1 = spuMapper.selectOne(new LambdaQueryWrapper<SpuInfo>()
                .eq(SpuInfo::getSpuName, spuInfo.getSpuName())
                .eq(SpuInfo::getDeleteFlag, 0));
        if(spuInfo1!=null) throw new SystemException(ResultCode.FAILED);
        int r = spuMapper.insert(spuInfo);
        if(r==1) return R.ok();
        return R.fail();
    }

    public R<List<SpuInfo>> getSpuList() {
        List<SpuInfo> spuInfos = spuMapper.selectList(new LambdaQueryWrapper<SpuInfo>()
                .eq(SpuInfo::getDeleteFlag, 0)
                .orderByDesc(SpuInfo::getCreateTime));
        return R.ok(spuInfos);
    }

    public R<List<SkuInfo>> getDetail(Long spuId) {
        List<SkuInfo> skuInfos = skuMapper.selectList(new LambdaQueryWrapper<SkuInfo>()
                .eq(SkuInfo::getSpuId, spuId)
                .eq(SkuInfo::getDeleteFlag, 0));
        return R.ok(skuInfos);
    }

    public R<Boolean> addSpuSku(Long skuId, Long spuId) {
        SkuInfo skuInfo = skuMapper.selectOne(new LambdaQueryWrapper<SkuInfo>()
                .eq(SkuInfo::getId, skuId)
                .eq(SkuInfo::getDeleteFlag, 0));
        skuInfo.setSpuId(spuId);
        int row = skuMapper.updateById(skuInfo);
        if(row!=1) throw new SystemException(ResultCode.FAILED);
        SpuInfo spuInfo = spuMapper.selectOne(new LambdaQueryWrapper<SpuInfo>()
                .eq(SpuInfo::getId, spuId)
                .eq(SpuInfo::getDeleteFlag, 0));
        spuInfo.setTotalNumber(spuInfo.getTotalNumber()+1);
        int row1 = spuMapper.updateById(spuInfo);
        if(row1!=1) throw new SystemException(ResultCode.FAILED);
        return R.ok();
    }

    public R<Boolean> reduceSpuSku(Long skuId, Long spuId) {
        SkuInfo skuInfo = skuMapper.selectOne(new LambdaQueryWrapper<SkuInfo>()
                .eq(SkuInfo::getId, skuId)
                .eq(SkuInfo::getDeleteFlag, 0));
        skuInfo.setSpuId(0L);
        int row = skuMapper.updateById(skuInfo);
        if(row!=1) throw new SystemException(ResultCode.FAILED);
        SpuInfo spuInfo = spuMapper.selectOne(new LambdaQueryWrapper<SpuInfo>()
                .eq(SpuInfo::getId, spuId)
                .eq(SpuInfo::getDeleteFlag, 0));
        spuInfo.setTotalNumber(spuInfo.getTotalNumber()-1);
        int row1 = spuMapper.updateById(spuInfo);
        if(row1!=1) throw new SystemException(ResultCode.FAILED);
        return R.ok();
    }


    public R<Boolean> updateSpu(SpuInfo spuInfo) {
        SpuInfo spuInfo1 = spuMapper.selectOne(new LambdaQueryWrapper<SpuInfo>()
                .eq(SpuInfo::getId, spuInfo.getId())
                .eq(SpuInfo::getDeleteFlag, 0));
        if(spuInfo1==null) throw new SystemException(ResultCode.FAILED);
        int row = spuMapper.updateById(spuInfo);
        if(row!=1) throw new SystemException(ResultCode.FAILED);
        return R.ok();
    }

    public R<Boolean> deleteSpu(Long spuId) {
        SpuInfo spuInfo1 = spuMapper.selectOne(new LambdaQueryWrapper<SpuInfo>()
                .eq(SpuInfo::getId,spuId)
                .eq(SpuInfo::getDeleteFlag, 0));
        if(spuInfo1==null) throw new SystemException(ResultCode.FAILED);
        spuInfo1.setDeleteFlag(1);
        int row = spuMapper.updateById(spuInfo1);
        if(row!=1) throw new SystemException(ResultCode.FAILED);
        return R.ok();
    }
}
