package com.zqq.mybatisplus.controller;


import cn.hutool.core.bean.BeanUtil;
import com.zqq.mybatisplus.domain.dto.UserFormDTO;
import com.zqq.mybatisplus.domain.po.User;
import com.zqq.mybatisplus.domain.query.UserQuery;
import com.zqq.mybatisplus.domain.vo.UserVO;
import com.zqq.mybatisplus.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @ApiOperation("新增用户接口")
    @PostMapping
    public void saveUser(@RequestBody UserFormDTO userFormDTO){
        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        userService.save(user);
    }

    @ApiOperation("删除用户接口")
    @DeleteMapping("{id}")
    public void deleteUserById(@ApiParam("用户id") @PathVariable("id") Long id){
        userService.removeById(id);
    }

    @ApiOperation("查询用户接口")
    @GetMapping("{id}")
    public UserVO getUserById(@ApiParam("用户id") @PathVariable("id") Long id){
        User user = userService.getById(id);
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @ApiOperation("批量查询用户接口")
    @GetMapping
    public List<UserVO> getUserByIds(@ApiParam("用户id集合") @RequestParam("ids") List<Long> ids){
        List<User> users = userService.listByIds(ids);
        return BeanUtil.copyToList(users, UserVO.class);
    }

    @ApiOperation("扣减用户余额接口")
    @PutMapping("/{id}/deduction/{money}")
    public void deductBalance(
            @ApiParam("用户id") @PathVariable("id") Long id,
            @ApiParam("扣减的金额") @PathVariable("money") Integer money){
        userService.deductBalance(id, money);
    }

    @ApiOperation("根据复杂条件查询用户接口")
    @GetMapping("/list")
    public List<UserVO> queryUsers(UserQuery query){
        // 1.查询用户PO
        List<User> users = userService.queryUsers(
                query.getName(), query.getStatus(), query.getMinBalance(), query.getMaxBalance());
        // 2.把PO拷贝到VO
        return BeanUtil.copyToList(users, UserVO.class);
    }

}
