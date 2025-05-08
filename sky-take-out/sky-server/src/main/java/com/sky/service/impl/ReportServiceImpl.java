package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 统计指定时间内的营业额数据
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistic(LocalDate begin, LocalDate end) {
//        当前集合用户存放begin到end范围内的每天日期
        List<LocalDate> dateList=new ArrayList<>();

        dateList.add(begin);

        while (!begin.equals(end)){
//            计算指定日期后一天
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

//        存放每天营业额
        List<Double> turnoverList=new ArrayList<>();
        for (LocalDate date : dateList) {
//            查询date日期对应的营业额数据，营业额是指：状态为“已完成”的订单合计
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
//            select sum(amount) from orders where order_time > ? and order_time < ? and status = 5
            Map map=new HashMap();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover=orderMapper.sumByMap(map);
            turnover=turnover == null?0.0:turnover;
            turnoverList.add(turnover);
        }

//        封装返回结果
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(turnoverList,","))
                .build();
    }

    /**
     * 统计用户
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistic(LocalDate begin, LocalDate end) {
//        存放begin到end之间每天对应的日期
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);

        while (!begin.equals(end)){
//            计算指定日期后一天
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        //每天新增用户数量 select count(id) from user where create_time < ? and create_time > ?
        List<Integer> newUserList=new ArrayList<>();
        //每天总用户数量 select count(id) from user where create_time < ?
        List<Integer> totalUserList=new ArrayList<>();

        for (LocalDate date : dateList) {
//
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map=new HashMap();
            map.put("end",endTime);
//            总数量
            Integer totalUser = userMapper.countByMap(map);
//            新增数量
            map.put("begin",beginTime);
            Integer newUser = userMapper.countByMap(map);

            totalUserList.add(totalUser);
            newUserList.add(newUser);
        }
//        封装数据
        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .build();

    }


}
