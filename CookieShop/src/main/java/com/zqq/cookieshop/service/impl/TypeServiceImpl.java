package com.zqq.cookieshop.service.impl;

import com.zqq.cookieshop.mapper.TypeMapper;
import com.zqq.cookieshop.service.ITypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TypeServiceImpl implements ITypeService {

    private final TypeMapper typeMapper;

}
