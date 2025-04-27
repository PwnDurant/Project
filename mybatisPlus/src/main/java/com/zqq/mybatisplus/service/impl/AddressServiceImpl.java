package com.zqq.mybatisplus.service.impl;

import com.zqq.mybatisplus.domain.po.Address;
import com.zqq.mybatisplus.mapper.AddressMapper;
import com.zqq.mybatisplus.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chase
 * @since 2025-04-27
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}
