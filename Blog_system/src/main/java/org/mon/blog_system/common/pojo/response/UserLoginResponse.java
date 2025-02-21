package org.mon.blog_system.common.pojo.response;


import lombok.Data;

@Data
public class UserLoginResponse {
    private Integer userId;
    private String token;
}
