package com.zyp.note.pojo.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 便签信息类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NoteInfo extends BaseEntity{

    /**
     * 便签组键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 每一份便签对应的用户Id
     */
    private Long user_id;

    /**
     * 便签标题
     */
    private String title;

    /**
     * 便签内容
     */
    private String content;

}
