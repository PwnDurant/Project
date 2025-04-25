package com.zqq.javaailangchain4j.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zqq.javaailangchain4j.entity.Appointment;

public interface AppointmentService extends IService<Appointment> {

    Appointment getOne(Appointment appointment);

}
