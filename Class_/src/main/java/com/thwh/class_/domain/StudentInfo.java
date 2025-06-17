package com.thwh.class_.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentInfo extends BaseEntity{

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String studentName;

    private Long classId;

}
