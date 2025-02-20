package org.mon.blog_system.common.pojo.dataobject;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.time.LocalDate;  //只有日期
import java.time.LocalDateTime;  //既有日期又有时间
import java.util.Date;

@Data
public class BlogInfo {

//    设置主键自增。声明类型为IdType
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private Integer userId;
    private Integer deleteFlag;
    private LocalDateTime createTime;
    private Date updateTime;


//    格式化日期
//    这个不支持
//    public String getCreateTime(){
//        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return dateFormat.format(createTime);
//    }



}
