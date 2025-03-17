package com.zqq.blog.service;


import com.zqq.api.pojo.AddBlogInfoRequest;
import com.zqq.api.pojo.BlogInfoResponse;
import com.zqq.api.pojo.UpBlogRequest;

import java.util.List;

public interface BlogService {
    List<BlogInfoResponse> getList();

    BlogInfoResponse getBlogDetail(Integer blogId);

    Boolean addBlog(AddBlogInfoRequest addBlogInfoRequest);

    Boolean update(UpBlogRequest upBlogRequest);

    Boolean delete(Integer blogId);
}
