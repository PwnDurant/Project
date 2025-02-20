package org.mon.blog_system.service;

//把service单独写成一个接口然后供别人调用，具体实现在专门的实现类中实现
//这是一种SOA的设计风格


import org.mon.blog_system.common.pojo.response.BlogInfoResponse;

import java.util.List;

public interface BlogService {

    List<BlogInfoResponse> getList();

    BlogInfoResponse getBlogDetail(Integer blogId);
}
