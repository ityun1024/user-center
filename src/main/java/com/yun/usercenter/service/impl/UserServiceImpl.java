package com.yun.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yun.usercenter.common.ErrorCode;
import com.yun.usercenter.exception.BusinessException;
import com.yun.usercenter.model.domain.User;
import com.yun.usercenter.service.UserService;
import com.yun.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.yun.usercenter.constant.UserConstant.USER_LOGIN_STATUS;


/**
 * 用户服务实现类
 *
* @author yun
* @description 针对表【user(用户信息表)】的数据库操作Service实现
* @createDate 2024-11-02 22:25:15
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "yun";


    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于4位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度小于8位");
        }
        // 账户只包含数字和大小写字母 不包含任何特殊字符
        String regex = "^[a-zA-Z0-9]*$";
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);
        if (!matcher.find()) {
            return -1L;
        }
        if(!userPassword.equals(checkPassword)) {
            return -1L;
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1L;
        }

        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1L;
        }
        //4.返回
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount,userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于4位");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度小于8位");
        }
        // 账户只包含数字和大小写字母 不包含任何特殊字符
        String regex = "^[a-zA-Z0-9]*$";
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号格式错误");
        }
        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, user_account user_password does not match！");
            return null;
        }
        //3.用户脱敏
        User saftyUser = getSaftyUser(user);
        //3.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATUS, saftyUser);
        //4.返回脱敏后的用户信息
        return saftyUser;
    }

    @Override
    public User getSaftyUser(User originalUser) {
        if (originalUser == null) {
            return null;
        }
        User saftyUser = new User();
        saftyUser.setId(originalUser.getId());
        saftyUser.setNickname(originalUser.getNickname());
        saftyUser.setUserAccount(originalUser.getUserAccount());
        saftyUser.setAvatarUrl(originalUser.getAvatarUrl());
        saftyUser.setGender(originalUser.getGender());
        saftyUser.setPhone(originalUser.getPhone());
        saftyUser.setEmail(originalUser.getEmail());
        saftyUser.setUserStatus(originalUser.getUserStatus());
        saftyUser.setUserRole(originalUser.getUserRole());
        saftyUser.setCreator(originalUser.getCreator());
        saftyUser.setCreateTime(originalUser.getCreateTime());
        saftyUser.setModifier(originalUser.getModifier());
        saftyUser.setModifyTime(originalUser.getModifyTime());
        return saftyUser;
    }

    @Override
    public List<User> findByUsername(String nickname) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(nickname)) {
            queryWrapper.like("nickname", nickname);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        return userList.stream().map(this::getSaftyUser).collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(long id) {
        int result = userMapper.deleteById(id);
        return result > 0;
    }

    /**
     * 用户退出登录
     * @param request 请求
     * @return 1 成功 0 失败
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除用户登录态
        request.getSession().removeAttribute(USER_LOGIN_STATUS);
        return 1;
    }
}




