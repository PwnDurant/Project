package com.why.library.dao;


import lombok.Data;

@Data
public class PageRequest {
//    关于偏移量的一些信息

//    先定义好 到时候可以直接进行传入参数
    private Integer currentPage=1;
    private Integer pageSize=10;

    private Integer offset;

    public Integer getOffset(){
        return (currentPage-1)*pageSize;
    }
}
