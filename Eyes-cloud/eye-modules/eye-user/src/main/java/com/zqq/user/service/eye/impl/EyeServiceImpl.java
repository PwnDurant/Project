package com.zqq.user.service.eye.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zqq.user.domain.eye.Eye;
import com.zqq.user.domain.eye.vo.EyeVO;
import com.zqq.user.mapper.eye.EyeMapper;
import com.zqq.user.service.eye.IEyeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EyeServiceImpl implements IEyeService {

    @Autowired
    private EyeMapper eyeMapper;

    /**
     * 根据疾病名称返回疾病信息
     * @param name 疾病名称
     * @return 返回疾病信息
     */
    @Override
    public EyeVO detail(String name) {
        Eye eye= eyeMapper.selectOne(new LambdaQueryWrapper<Eye>()
                .eq(Eye::getName,name));
        EyeVO eyeVO=new EyeVO();
        BeanUtils.copyProperties(eye,eyeVO);
        return eyeVO;
    }
}
