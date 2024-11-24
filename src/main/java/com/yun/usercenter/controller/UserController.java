package com.yun.usercenter.controller;


import com.yun.usercenter.common.BaseResponse;
import com.yun.usercenter.common.ErrorCode;
import com.yun.usercenter.common.ResultUtils;
import com.yun.usercenter.constant.UserConstant;
import com.yun.usercenter.exception.BusinessException;
import com.yun.usercenter.model.domain.User;
import com.yun.usercenter.model.domain.request.UserLoginRequest;
import com.yun.usercenter.model.domain.request.UserRegisterRequest;
import com.yun.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result, "注册成功");
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户或密码不能为空");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        if (user == null) {
            throw new BusinessException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }


    /**
     * 获取当前登录用户
     * @param request request请求
     * @return 用户信息
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATUS);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
        }
        Long userId = currentUser.getId();
        // 校验用户是否合法
        User user = userService.getById(userId);
        User saftyUser = userService.getSaftyUser(user);
        return ResultUtils.success(saftyUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUserList(String nickname, HttpServletRequest request) {
        log.info("请求入参：{}", nickname);
        // 仅管理员可查询
        if (!checkUserRole(request)) {
            return ResultUtils.success(new ArrayList<>());
        }
        List<User> userList = userService.findByUsername(nickname);
        return ResultUtils.success(userList);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(long id, HttpServletRequest request) {
        if (!checkUserRole(request)) {
            throw new BusinessException(ErrorCode.NO_AUTHOR);
        }
        if (id <= 0) {
            return ResultUtils.fail(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.deleteById(id);
        return ResultUtils.success(result);
    }

    /**
     * 校验用户角色是否为管理员
     *
     * @param request request请求
     * @return 是否为管理员
     */
    private boolean checkUserRole(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATUS);
        User user =  (User) userObj;
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }

}
