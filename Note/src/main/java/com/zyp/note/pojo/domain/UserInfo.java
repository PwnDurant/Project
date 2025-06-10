package com.zyp.note.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfo extends BaseEntity{

    /**
     * 每个用户对应的主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long Id;

    /**
     * 每个用户对应的便签id
     */
    private Long noteId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String password;

}
