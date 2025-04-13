package com.zqq.user.domain.user;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("eye_user")
public class User {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "USER_ID",type = IdType.ASSIGN_ID)
    private Long userId;         // 用户ID

    private String name;         // 姓名

    private Integer age;         // 年龄

    private String gender;       // 性别（1位字符，例如 M/F）

    private String phone;        // 手机号

    private String code;         // 验证码

    private String email;        // 邮箱

    @TableField(fill= FieldFill.INSERT)
    private LocalDateTime createTime; // 创建时间

    @TableField(fill=FieldFill.UPDATE)
    private LocalDateTime updateTime; // 更新时间
}
