package org.mon.library_management_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
//    关于返回的信息

//    图书总量
    private Integer total;
//    查询到的图书列表
    private List<T> records;
//    请求的偏移量的信息
    private PageRequest request;
}
