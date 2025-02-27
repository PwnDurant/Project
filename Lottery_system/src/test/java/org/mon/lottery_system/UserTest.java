package org.mon.lottery_system;


import org.junit.jupiter.api.Test;
import org.mon.lottery_system.service.UserService;
import org.mon.lottery_system.service.dto.UserDTO;
import org.mon.lottery_system.service.enums.UserIdentityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;


    @Test
    void findBaseUserList(){

        List<UserDTO> userInfo = userService.findUserInfo(UserIdentityEnum.ADMIN);
        for(UserDTO userDTO:userInfo){
            System.out.println(userDTO.getUserId()+" "+userDTO.getUserName()+" "+userDTO.getIdentity());
//            System.out.println();
//            System.out.println();
        }
    }
}
