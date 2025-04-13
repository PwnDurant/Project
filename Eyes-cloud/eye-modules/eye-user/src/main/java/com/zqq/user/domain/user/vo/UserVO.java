package com.zqq.user.domain.user.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 用户展示信息
 */
@Data
public class UserVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;         // 用户ID

    private String name;         // 姓名

    private Integer age;         // 年龄

    private String gender;       // 性别（1位字符，例如 M/F）

    private String phone;        // 手机号

    private String email;        // 邮箱

}
