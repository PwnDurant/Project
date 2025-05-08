package com.sky.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MyTask {

    /**
     * 定时任务，每5秒触发一次
     */
    @Scheduled(cron = "0/5 * * * * ?") //秒，分钟，小时，日(?)，月，周(?)，年（可选）
    public void executeTask(){
        log.info("定时任务开始执行...{}",new Date());
    }

}
