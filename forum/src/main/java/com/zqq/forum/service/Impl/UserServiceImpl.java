package com.zqq.forum.service.Impl;

import com.zqq.forum.common.AppResult;
import com.zqq.forum.common.ResultCode;
import com.zqq.forum.dao.UserMapper;
import com.zqq.forum.exception.ApplicationException;
import com.zqq.forum.model.Board;
import com.zqq.forum.model.User;
import com.zqq.forum.service.IUserService;
import com.zqq.forum.utils.MD5Util;
import com.zqq.forum.utils.StringUtil;
import com.zqq.forum.utils.UUIDUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Date;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void createNormalUser( User user) {
//        非空校验
        if(user==null||StringUtil.isEmpty(user.getUsername()
        )||StringUtil.isEmpty(user.getNickname())||StringUtil.isEmpty(user.getPassword())||
        StringUtil.isEmpty(user.getSalt())){
//            打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
//            抛出异常,统一抛出ApplicationException
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

//        按用户名查询用户信息
        User exitsUser = userMapper.selectByUserName(user.getUsername());

//        判断用户是否存在
        if(exitsUser!=null){
            log.info(ResultCode.FAILED_USER_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_EXISTS));
        }

//        新增用户,设置默认值
        user.setGender((byte) 2);
        user.setArticleCount(0);
        user.setIsAdmin((byte)0);
        user.setState((byte) 0);
        user.setDeleteState((byte) 0);

        Date date=new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);

//        写入数据库
        int i = userMapper.insertSelective(user);
        if(i!=1){
            log.info(ResultCode.FAILED_CREATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }

//        正常
        log.info("新增用户成功,username={}.",user.getUsername());

    }

    @Override
    public User selectByUserName(String username) {

        if(StringUtil.isEmpty(username)){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        return userMapper.selectByUserName(username);

    }

    @Override
    public User login(String username, String password) {

        if(StringUtil.isEmpty(username)||StringUtil.isEmpty(password)){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }

        User user=selectByUserName(username);

        if(user==null){
            log.warn(ResultCode.FAILED_LOGIN.toString()+",username="+username);
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }

        String encryptPassword = MD5Util.md5Salt(password, user.getSalt());

        if(!user.getPassword().equals(encryptPassword)){
            log.warn(ResultCode.FAILED_LOGIN.toString()+"，密码错误,username="+username);
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }

        log.info("登入成功:username={}",username);

        return user;
    }

    @Override
    public User selectById(Long id) {
        if(id==null){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOneArticleCountById(Long id) {
        if(id==null||id<=0){
            log.warn(ResultCode.FAILED_BOARD_ARTICLE_COUNT.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT));
        }

        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            log.warn(ResultCode.ERROR_IS_NULL.toString()+",user id = "+id);
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }

//        设置要更新的属性进行更新，不需要全部更新
        User updateUser=new User();
        updateUser.setId(user.getId());
        updateUser.setArticleCount(user.getArticleCount()+1);
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if(row!=1){
            log.warn(ResultCode.ERROR_SERVICES.toString()+"受影响函数不等于1.");
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }

    }

    @Override
    public void subOneArticleCountById(Long id) {
        if(id==null||id<=0){
            log.warn(ResultCode.FAILED_BOARD_ARTICLE_COUNT.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT));
        }

        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            log.warn(ResultCode.ERROR_IS_NULL.toString()+",user id = "+id);
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }

        User updateUser=new User();
        updateUser.setId(user.getId());
        updateUser.setArticleCount(user.getArticleCount()-1);

        if(updateUser.getArticleCount()<0){
            updateUser.setArticleCount(0);
        }

        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if(row!=1){
            log.warn(ResultCode.ERROR_SERVICES.toString()+"受影响函数不等于1.");
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }

    }

    @Override
    public void modifyInfo(User user) {

        if(user==null||user.getId()==null||user.getId()<=0){
            log.warn(ResultCode.FAILED_BOARD_ARTICLE_COUNT.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT));
        }

        User existsUser = userMapper.selectByPrimaryKey(user.getId());
        if(existsUser==null){
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        boolean checkAttr=false;

        User updateUser=new User();
        updateUser.setId(user.getId());

        if(!StringUtil.isEmpty(user.getUsername())&&!existsUser.getUsername().equals(user.getUsername())){
            User checkUser = userMapper.selectByUserName(user.getUsername());
            if(checkUser!=null){
                log.warn(ResultCode.FAILED_USER_EXISTS.toString());
                throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_EXISTS));
            }
            updateUser.setUsername(user.getUsername());
            checkAttr=true;
        }

        if(!StringUtil.isEmpty(user.getNickname())&&!existsUser.getUsername().equals(user.getNickname())){
            updateUser.setNickname(user.getNickname());
            checkAttr=true;
        }

        if(user.getGender()!=null&&existsUser.getGender() != user.getGender()){
            updateUser.setGender(user.getGender());
            if(updateUser.getGender()>2||updateUser.getGender()<0){
                updateUser.setGender((byte)2);
            }
            checkAttr=true;
        }

        if(!StringUtil.isEmpty(user.getEmail())&&!user.getEmail().equals(existsUser.getEmail())){
            updateUser.setEmail(user.getEmail());
            checkAttr=true;
        }

        if(!StringUtil.isEmpty(user.getPhoneNum())&&!user.getPhoneNum().equals(existsUser.getPhoneNum())){
            updateUser.setPhoneNum(user.getPhoneNum());
            checkAttr=true;
        }

        if(!StringUtil.isEmpty(user.getRemark())&&!user.getRemark().equals(existsUser.getRemark())){
            updateUser.setRemark(user.getRemark());
            checkAttr=true;
        }

        if(!checkAttr){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if(row!=1){
            log.warn(ResultCode.ERROR_SERVICES.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }

    }

    @Override
    public void modifyPassword(Long id, String newPassword, String oldPassword) {
        
        if(id==null||id<=0||StringUtil.isEmpty(newPassword)||StringUtil.isEmpty(oldPassword)){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        User user = userMapper.selectByPrimaryKey(id);
        if(user==null||user.getDeleteState()==1){
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        String oldEncryptPassword = MD5Util.md5Salt(oldPassword, user.getSalt());
        if(!oldEncryptPassword.equalsIgnoreCase(user.getPassword())){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        
        String salt= UUIDUtil.UUID_32();
        String newEncryptPassword = MD5Util.md5Salt(newPassword, salt);

        User updateUser=new User();
        updateUser.setId(user.getId());
        updateUser.setSalt(salt);
        updateUser.setPassword(newEncryptPassword);
        updateUser.setUpdateTime(new Date());

        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if(row!=1){
            log.warn(ResultCode.FAILED.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }

    }

}
