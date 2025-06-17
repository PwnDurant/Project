package com.ygt.park.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ygt.park.common.base.R;
import com.ygt.park.common.base.ResultCode;
import com.ygt.park.common.exception.SystemException;
import com.ygt.park.domain.SysInfo;
import com.ygt.park.mapper.SysMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysService {

    @Autowired
    private SysMapper sysMapper;

    public R<Boolean> login(SysInfo sysInfo) {
        SysInfo sysInfo1 = sysMapper.selectOne(new LambdaQueryWrapper<SysInfo>()
                .eq(SysInfo::getUserName, sysInfo.getUserName())
                .eq(SysInfo::getDeleteFlag, 0));
        if(sysInfo1==null) throw new SystemException(ResultCode.FAILED);
        if(sysInfo.getPassword().equals(sysInfo1.getPassword())) return R.ok();
        return R.fail();
    }

    public Boolean register(SysInfo sysInfo){
        SysInfo sysInfo1 = sysMapper.selectOne(new LambdaQueryWrapper<SysInfo>()
                .eq(SysInfo::getUserName, sysInfo.getUserName())
                .eq(SysInfo::getDeleteFlag, 0));
        if(sysInfo1!=null) throw new SystemException(ResultCode.FAILED);
        int insert = sysMapper.insert(sysInfo);
        return insert==1;
    }
}
