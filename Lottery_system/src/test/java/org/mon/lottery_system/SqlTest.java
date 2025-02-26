package org.mon.lottery_system;


import org.junit.jupiter.api.Test;
import org.mon.lottery_system.dao.dataobject.Encrypt;
import org.mon.lottery_system.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SqlTest {

    @Autowired
    private UserMapper userMapper;


    @Test
    void mailCount(){
        int count= userMapper.countByMail("123@qq.com");
        System.out.println(count);
    }

    @Test
    void countByPhone() {
        int count= userMapper.countByPhone(new Encrypt("13111111111"));
        System.out.println(1);
    }
}
