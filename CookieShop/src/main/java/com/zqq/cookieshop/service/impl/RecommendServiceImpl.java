package com.zqq.cookieshop.service.impl;

import com.zqq.cookieshop.mapper.RecommendMapper;
import com.zqq.cookieshop.service.IRecommendService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RecommendServiceImpl implements IRecommendService {

    private final RecommendMapper recommendMapper;

}
