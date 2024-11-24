package com.yun.usercenter.common;

import lombok.Getter;

/**
 * 错误码枚举类
 *
 * @author yunhu
 */
@Getter
public enum ErrorCode {

    SUCCESS(0, "success", "操作成功"),
    PARAMS_ERROR(40000, "请求参数错误", "参数错误"),
    PARAMS_NULL_ERROR(40001, "请求参数为空", "参数不能为空"),
    NO_LOGIN(40100, "未登录", "请先登录"),
    NO_AUTHOR(40300, "无权限", "没有该接口的访问权限"),
    SERVER_ERROR(50000, "服务器异常", "系统错误"),
    ACCOUNT_PASSWORD_ERROR(50001, "用户名或密码错误", "用户名或密码错误"),
    ACCOUNT_NOT_EXIST(50002, "用户不存在", "用户不存在"),
    ACCOUNT_LOCKED(50003, "账号被冻结", "账号被冻结"),
    TOKEN_EXPIRE(60100, "token已过期", "token已过期"),
    TOKEN_ERROR(60101, "token错误", "token错误");


    private final int code;

    /**
     * 错误码信息
     */
    private final String message;

    /**
     * 错误码描述
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

}
