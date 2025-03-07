package org.mon.gobang.api;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.mon.gobang.model.User;
import org.mon.gobang.model.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserAPI {

    @Autowired
    private UserMapper userMapper;


    @PostMapping("/login")
    @ResponseBody
    public Object login(String username, String password, HttpServletRequest req){
//        关键操作，就是根据username去数据库中查询
        User user=userMapper.selectByName(username);
        log.info("[login] user:{}",user);

        if(user==null||!user.getPassword().equals(password)){
//            登入失败
            log.warn("登入失败");
            return new User();
        }
        HttpSession httpSession=req.getSession(true);
        httpSession.setAttribute("user",user);
        return user;
    }

    @PostMapping("/register")
    @ResponseBody
    public Object register(String username,String password){
        try{
            User user=new User();
            user.setUsername(username);
            user.setPassword(password);
            userMapper.insert(user);
            return user;
        }catch (org.springframework.dao.DuplicateKeyException e){
            return new User();
        }
    }

    @GetMapping("/userInfo")
    @ResponseBody
    public Object getUserInfo(HttpServletRequest request){
        try{
            HttpSession httpSession= request.getSession(false);
            User user=(User)httpSession.getAttribute("user");
//            拿着这个user对象去数据库中找最新的对象
            return userMapper.selectByName(user.getUsername());
        }catch (NullPointerException e){
            return new User();
        }
    }

}
