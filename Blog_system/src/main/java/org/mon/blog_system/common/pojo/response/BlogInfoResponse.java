package org.mon.blog_system.common.pojo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;//显示的不是东八时区

@Data
public class BlogInfoResponse {
    private Integer userId;
    private Integer id;
    private String title;
    private String content;
//    private Integer userId;
//    private Integer deleteFlag;

//    这个是注解的方式，但是对于date类型需要加上时区
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    private Date updateTime;


//    对Date属性的时间格式进行格式化
//    public String getUpdateTime(){
//        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return dateFormat.format(updateTime);
//    }

//    设置返回的内容
//    public String getContent(){
//        return content==null?"":content.substring(0,50);
//    }




}
