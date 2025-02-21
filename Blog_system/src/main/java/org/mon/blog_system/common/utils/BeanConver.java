package org.mon.blog_system.common.utils;

import org.mon.blog_system.common.pojo.dataobject.BlogInfo;
import org.mon.blog_system.common.pojo.dataobject.UserInfo;
import org.mon.blog_system.common.pojo.response.BlogInfoResponse;
import org.mon.blog_system.common.pojo.response.UserInfoResponse;
import org.springframework.beans.BeanUtils;

public class BeanConver {
    public static BlogInfoResponse trans(BlogInfo blogInfo){
        BlogInfoResponse blogInfoResponse=new BlogInfoResponse();
//        这里源不能为null
        if(blogInfo!=null) BeanUtils.copyProperties(blogInfo,blogInfoResponse);
        return blogInfoResponse;
    }

    public static UserInfoResponse trans(UserInfo userInfo){
        UserInfoResponse userInfoResponse=new UserInfoResponse();
//        这里源不能为null
        if(userInfo!=null) BeanUtils.copyProperties(userInfo,userInfoResponse);
        return userInfoResponse;
    }
}
