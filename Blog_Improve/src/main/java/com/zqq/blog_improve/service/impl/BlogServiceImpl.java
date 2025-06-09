package com.zqq.blog_improve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zqq.blog_improve.common.base.R;
import com.zqq.blog_improve.common.base.ResultCode;
import com.zqq.blog_improve.common.constant.Constants;
import com.zqq.blog_improve.common.exception.BlogException;
import com.zqq.blog_improve.common.pojo.domain.BaseEntity;
import com.zqq.blog_improve.common.pojo.domain.BlogInfo;
import com.zqq.blog_improve.common.pojo.dto.AddBlogDTO;
import com.zqq.blog_improve.common.pojo.dto.UpBlogDTO;
import com.zqq.blog_improve.common.pojo.vo.BlogInfoVO;
import com.zqq.blog_improve.common.utils.RedisService;
import com.zqq.blog_improve.mapper.BlogMapper;
import com.zqq.blog_improve.service.IBlogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BlogServiceImpl implements IBlogService {

    @Resource(name = "redisService")
    private RedisService redisService;

    private final BlogMapper blogMapper;

    public BlogServiceImpl(BlogMapper blogMapper){
        this.blogMapper=blogMapper;
    }

    /**
     * 获取博客列表
     * @return 返回博客信息列表
     */
    @Override
    public R<List<BlogInfoVO>> getList() {
        List<BlogInfo> cacheObject=null;
//        先从缓存中查询，如果缓存没有的话再从数据库中查询
        try {
            cacheObject = redisService.getCacheList(Constants.BLOG_LIST, new TypeReference<List<BlogInfo>>() {
            });
        }catch (Exception e){
            log.error("从缓存中读取数据失败");
        }
        if(cacheObject==null|| cacheObject.isEmpty()){
            log.warn("缓存中没有数据从数据库中读取，并刷新缓存");
            return getListFromDB();
        }
        List<BlogInfoVO> blogInfoVOList=BeanUtil.copyToList(cacheObject,BlogInfoVO.class);
        return R.ok(blogInfoVOList);
    }

    private R<List<BlogInfoVO>> getListFromDB() {
        //        从数据库中获取博客数据，并按照降序排列
        List<BlogInfo> blogInfoList = blogMapper.selectList(new LambdaQueryWrapper<BlogInfo>()
                .eq(BaseEntity::getDeleteFlag, 0)
                .orderByDesc(BlogInfo::getUpdateTime));
        if(blogInfoList.isEmpty()){
            log.error("数据库中没有博客列表信息...");
            throw new BlogException(ResultCode.BLOG_RESOURCES_IS_EMPTY);
        }
//        刷新缓存，将数据库查到的数据添加到缓存中 -- 这是一条一条插入 也可以一次性批量插入
//        blogInfoList.forEach(blogInfo -> {
//            redisService.leftPush(Constants.BLOG_LIST,blogInfo);
//        });
//        批量插入缓存
        redisService.leftPushAll(blogInfoList);
        List<BlogInfoVO> blogInfoVOList = BeanUtil.copyToList(blogInfoList, BlogInfoVO.class);
        return R.ok(blogInfoVOList);
    }

    /**
     * 获取博客详情
     * @param blogId 博客Id
     * @return 返回博客详情
     */
    @Override
    public R<BlogInfoVO> getBlogDetail(Long blogId) {
        BlogInfo blogInfo = blogMapper.selectOne(new LambdaQueryWrapper<BlogInfo>()
                .eq(BlogInfo::getId, blogId)
                .eq(BlogInfo::getDeleteFlag, 0));
        if(blogInfo==null){
            log.error("对应博客信息为空,blog:{}",blogId);
            throw new BlogException(ResultCode.BLOG_IS_NOT_EXIST);
        }
        BlogInfoVO blogInfoVO = new BlogInfoVO();
        BeanUtil.copyProperties(blogInfo,blogInfoVO);
        return R.ok(blogInfoVO);
    }

    /**
     * 添加博客
     * @param addBlogDTO 添加博客传递的参数
     * @return 是否添加成功
     */
    @Override
    public R<Boolean> addBlog(AddBlogDTO addBlogDTO) {
//        在数据库中插入一份
        BlogInfo blogInfo = new BlogInfo();
        BeanUtil.copyProperties(addBlogDTO,blogInfo);
        int insert=0;
        try {
            insert = blogMapper.insert(blogInfo);
        }catch (Exception e){
            log.error("博客插入失败,e:",e);
        }
        if(insert==1) {
            //        在缓存中插入一份
            redisService.leftPush(Constants.BLOG_LIST,blogInfo);
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 更新博客
     * @param upBlogDTO 更新的数据
     * @return 是否更新成功
     */
    @Override
    public R<Boolean> updateBlog(UpBlogDTO upBlogDTO) {
//        判断博客是否存在
        BlogInfo blogInfo1 = blogMapper.selectOne(new LambdaQueryWrapper<BlogInfo>()
                .eq(BlogInfo::getId, upBlogDTO.getId())
                .eq(BlogInfo::getDeleteFlag, 0));
        if(blogInfo1==null) {
            log.error("需要更新的博客不存在！");
            throw new BlogException(ResultCode.BLOG_IS_NOT_EXIST);
        }
        BlogInfo blogInfo = new BlogInfo();
        BeanUtil.copyProperties(upBlogDTO,blogInfo);
//        把缓存中所有数据删除
        redisService.deleteKey(Constants.BLOG_LIST);
        try {
            int row = blogMapper.updateById(blogInfo);
            if(row==1) return R.ok();
        }catch (Exception e){
            log.error("博客更新失败,e:",e);
        }
        return R.fail();
    }

    @Override
    public R<Boolean> deleteBlog(Long blogId) {
        BlogInfo blogInfo = blogMapper.selectOne(new LambdaQueryWrapper<BlogInfo>()
                .eq(BlogInfo::getId, blogId)
                .eq(BlogInfo::getDeleteFlag, 0));
        if(blogInfo==null) {
            log.error("需要删除的博客已经被删除了！");
            throw new BlogException(ResultCode.BLOG_IS_NOT_EXIST);
        }
        blogInfo.setDeleteFlag(1);
//        把缓存中所有数据删除
        redisService.deleteKey(Constants.BLOG_LIST);
        int row = blogMapper.updateById(blogInfo);
        return row==1?R.ok():R.fail();
    }
}
