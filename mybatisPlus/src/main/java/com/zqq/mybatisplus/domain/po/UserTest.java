package com.zqq.mybatisplus.domain.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("tb_user")
public class UserTest {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @TableField(value = "username")
    private String name;

    @TableField("is_married")
    private boolean isMarried;

    @TableField("`order`")
    private Integer order;

    @TableField(exist = false)
    private String address;

}
