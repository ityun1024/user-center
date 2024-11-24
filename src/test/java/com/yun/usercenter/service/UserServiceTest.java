package com.yun.usercenter.service;

import com.yun.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author yun
 * 用户服务测试
 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testRegister() {
        String userAccount = "amoxilin";
        String userPassword = "";
        String checkPassword = "123456";
        // 非空
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1L, result);
        userAccount = "yun";
        userPassword = "123456";
        // 账户长度不小于4位
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount = "amoxilin";
        userPassword = "123456";
        // 密码长度不小于8位
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount = "amo xi lin";
        userPassword = "12345678";
        // 账户不包含特殊字符
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        // 密码和校验密码相同
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        // 账户不能重复
        userAccount = "1234567890";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //
        userAccount = "amoxilin";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result > 0);


    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setNickname("yun");
        user.setUserAccount("1234567890");
        user.setAvatarUrl("https://www.baidu.com");
        user.setGender(0);
        user.setUserPassword("123456789");
        user.setPhone("12345678901");
        user.setEmail("1234567890@qq.com");
        boolean result = userService.save(user);
        System.out.println("保存结果：" + user.getId());
        Assertions.assertTrue(result);
    }



}