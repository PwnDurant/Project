package com.zqq.user.domain.user.dto;

import lombok.Data;

/**
 * 用户需要编辑的信息
 */
@Data
public class UserUpdateDTO {

    private String name;         // 姓名

    private Integer age;         // 年龄

    private String gender;       // 性别（1位字符，例如 M/F）

    private String phone;        // 手机号

    private String email;        // 邮箱

}
