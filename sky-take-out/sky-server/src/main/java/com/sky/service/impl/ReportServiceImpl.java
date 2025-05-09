package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkspaceService workspaceService;

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

    /**
     * 统计订单
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderStatistic(LocalDate begin, LocalDate end) {
//        存放begin到end之间每天对应的日期
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);

        while (!begin.equals(end)){
//            计算指定日期后一天
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> orderCountList =new ArrayList<>();
        List<Integer> validCountList =new ArrayList<>();

//        便利dateList集合查询每一天有效订单数和总数
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
//            查询每天订单总数 select count(id) from orders where order_time > ? and order_time < ?
            Integer orderCount = getOrderCount(beginTime, endTime, null);
//            查询每天有效订单数据 select count(id) from orders where order_time > ? and order_time > ? and status = 5
            Integer validCount = getOrderCount(beginTime, endTime, Orders.COMPLETED);

            orderCountList.add(orderCount);
            validCountList.add(validCount);
        }

//        计算时间区间内的订单总数量
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
//        计算时间区间内的有效订单数量
        Integer validOrderCount = orderCountList.stream().reduce(Integer::sum).get();
//        订单完成率
        Double orderCompletionRate = 0.0;
        if(totalOrderCount!=0){
            orderCompletionRate=validOrderCount.doubleValue() /totalOrderCount;
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(orderCountList,","))
                .validOrderCountList(StringUtils.join(validCountList,","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .build();
    }

    /**
     * 销量排名
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);

        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String namesList = StringUtils.join(names, ",");

        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numbersList = StringUtils.join(numbers, ",");

//        封住返回结果数据
        return SalesTop10ReportVO.builder()
                .nameList(namesList)
                .numberList(numbersList)
                .build();
    }

    /**
     * 报表导出
     * @param response
     */
    @Override
    public void export(HttpServletResponse response) throws IOException {

//        查询数据库获取营业数据 ------> 查询最近30天的营业数据
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);

        LocalDateTime beginTime = LocalDateTime.of(dateBegin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(dateEnd, LocalTime.MAX);
//        查询概览数据
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(beginTime, endTime);

//        把查询到的数据通过 POI ，写入到 Excel 文件中
//        基于模版文件创建新的excel文件
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模版.xlsx");
        XSSFWorkbook excel = new XSSFWorkbook(input);

//        获取sheet标签页
        XSSFSheet sheet = excel.getSheet("sheet1");

//        填充数据
        sheet.getRow(1).getCell(1).setCellValue("时间："+dateBegin+"至"+dateEnd);

        XSSFRow row = sheet.getRow(3);
        row.getCell(2).setCellValue(businessDataVO.getTurnover());
        row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
        row.getCell(6).setCellValue(businessDataVO.getNewUsers());

        XSSFRow row5 = sheet.getRow(4);
        row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
        row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

//        填充明细数据
        for (int i = 0; i < 30; i++) {
            LocalDate date = dateBegin.plusDays(i);
//            查询某一天的营业数据
            BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

//            获得某一行
            row = sheet.getRow(7 + i);

            row.getCell(1).setCellValue(date.toString());
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(3).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(5).setCellValue(businessData.getUnitPrice());
            row.getCell(6).setCellValue(businessData.getNewUsers());

        }


//        通过输出流，将 excel 文件下载到浏览器
        ServletOutputStream out = response.getOutputStream();
        excel.write(out);

//        关闭资源
        out.close();
        excel.close();

    }

    /**
     * 根据条件统计订单数量
     * @param begin
     * @param end
     * @return
     */
    private Integer getOrderCount(LocalDateTime begin,LocalDateTime end,Integer status){
//        查询订单数量
        Map map=new HashMap();
        map.put("begin",begin);
        map.put("end",end);
        map.put("status",status);

        return orderMapper.countByMap(map);
    }

}
