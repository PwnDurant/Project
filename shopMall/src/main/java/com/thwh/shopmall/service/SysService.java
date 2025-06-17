package com.thwh.class_.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thwh.class_.common.base.R;
import com.thwh.class_.common.base.ResultCode;
import com.thwh.class_.common.exception.SystemException;
import com.thwh.class_.domain.SysInfo;
import com.thwh.class_.mapper.SysMapper;
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
