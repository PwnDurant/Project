package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReportService {

    /**
     * 统计指定时间内的营业额数据
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverStatistic(LocalDate begin,LocalDate end);

    /**
     * 统计用户
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatistic(LocalDate begin, LocalDate end);

}
