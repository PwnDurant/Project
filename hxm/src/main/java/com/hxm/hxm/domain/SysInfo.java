package com.thwh.shopmall.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysInfo extends BaseEntity{

    private Long id;

    private String userName;

    private String password;

}
