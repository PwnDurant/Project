package org.mon.blog_system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.mon.blog_system.common.pojo.Request.AddBlogParam;
import org.mon.blog_system.common.pojo.Request.UpBlogParam;
import org.mon.blog_system.common.pojo.dataobject.BlogInfo;
import org.mon.blog_system.common.pojo.dataobject.UserInfo;
import org.mon.blog_system.common.pojo.response.BlogInfoResponse;
import org.mon.blog_system.common.utils.BeanConver;
import org.mon.blog_system.mapper.BlogInfoMapper;
import org.mon.blog_system.service.BlogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//@Service("可以对bean进行从命名")
@Slf4j
@Service
public class BlogServiceImpl implements BlogService {


    @Autowired
    private BlogInfoMapper blogInfoMapper;

//    查询博客列表
    @Override
    public List<BlogInfoResponse> getList() {
//        查询数据库

//        这个是一个查询语句
        LambdaQueryWrapper<BlogInfo> queryWrapper=new LambdaQueryWrapper<BlogInfo>()
                .eq(BlogInfo::getDeleteFlag,0).orderByDesc(BlogInfo::getId);
//        这个方法返回的是查询结果
//        log.info("查询语句"+queryWrapper.toString());
        List<BlogInfo> blogInfos = blogInfoMapper.selectList(queryWrapper);
        log.info("查询结果:{}",blogInfos);

//        进行BlogInfoResponse和BlogInfo的转换
//        进行循环遍历
//           这是直接的方式，但是Bean提供了方法
//            blogInfoResponse.setId(blogInfo.getId());
//            blogInfoResponse.setTitle(blogInfo.getTitle());
//            blogInfoResponse.setContent(blogInfo.getContent());
//            blogInfoResponse.setCreateTime(blogInfo.getCreateTime());
//        把List对象转换为流然后用map进行循环，相对于用foreach，一个有返回结果，一个没有返回结果
//        List<BlogInfoResponse> blogInfoResponse=blogInfos.stream().map(blogInfo -> {
//            BlogInfoResponse blogResponse=new BlogInfoResponse();
//            BeanUtils.copyProperties(blogInfo,blogResponse);
//            return blogResponse;
//        }).collect(Collectors.toList());

//        抽取出公公的代码
        List<BlogInfoResponse> blogInfoResponse=blogInfos.stream().map(blogInfo ->BeanConver.trans(blogInfo)).collect(Collectors.toList());

        return blogInfoResponse;
    }

    @Override
    public BlogInfoResponse getBlogDetail(Integer blogId) {
//        调用查询方法
        BlogInfo blogInfo=selectBlogById(blogId);
//        调用转换方法
        BlogInfoResponse blogInfoResponse=BeanConver.trans(blogInfo);
        return blogInfoResponse;
    }

    @Override
    public Boolean addBlog(AddBlogParam addBlogParam) {
//        数据库是对BlogInfo进行操作的，所以要将对象转换为BlogInfo
//        BlogInfo blogInfo=new BlogInfo();
//        BeanUtils.copyProperties(addBlogParam,blogInfo);
        BlogInfo blogInfo = BeanConver.trans(addBlogParam);
        try{
            int insert = blogInfoMapper.insert(blogInfo);
            if(insert==1) return true;
        }catch (Exception e){
            log.error("博客插入失败,e:{}",e);
        }
        return false;
    }


//    更新博客
    @Override
    public Boolean updateBlog(UpBlogParam upBlogParam) {
//        对象转化
        BlogInfo blogInfo = BeanConver.trans(upBlogParam);
//        更新博客
        return  update(blogInfo);
    }

    @Override
    public Boolean deleteBlog(Integer blogId) {
        BlogInfo blogInfo=new BlogInfo();
        blogInfo.setId(blogId);
        blogInfo.setDeleteFlag(1);

        return update(blogInfo);
    }


    //    从数据库查询博客详情
    public BlogInfo selectBlogById(Integer blogId){
        return blogInfoMapper.selectOne(new LambdaQueryWrapper<BlogInfo>().eq(BlogInfo::getId, blogId).eq(BlogInfo::getDeleteFlag, 0));
    }


//    更新博客数据
    public Boolean update(BlogInfo blogInfo){
        try{
            Integer result = blogInfoMapper.updateById(blogInfo);
            if(result==1) return true;
            else return false;
        }catch (Exception e){
            log.error("更新博客失败,e:{}",e);
        }
        return false;
    }
}
