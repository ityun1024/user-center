package com.yun.usercenter.service;

import com.yun.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author yun
* @description 针对表【user(用户信息表)】的数据库操作Service
* @createDate 2024-11-02 22:25:15
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request 请求
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param user 用户信息
     * @return 脱敏后的用户信息
     */
    User getSaftyUser(User user);

    /**
     * 查询用户列表
     * @param nickname 用户昵称
     * @return 用户列表
     */
    List<User> findByUsername(String nickname);

    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否删除成功
     */
    boolean deleteById(long id);

    /**
     * 用户注销
     * @param request 请求
     * @return 是否注销成功
     */
    int userLogout(HttpServletRequest request);
}
