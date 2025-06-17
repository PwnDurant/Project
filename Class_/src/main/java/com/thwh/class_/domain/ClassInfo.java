package com.thwh.class_.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClassInfo extends BaseEntity{

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String className;

    private Integer leftNumber;

}
