package com.wpf.roomsimple.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class UserInfo {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String userName;

    private String password;

}
