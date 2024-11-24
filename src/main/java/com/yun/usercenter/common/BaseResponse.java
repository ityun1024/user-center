package com.yun.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 全局响应对象
 *
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 79294534396719876L;

    private int code;

    private String message;

    private T data;

    private String description;


    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(int code, String message, T data, String description) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.description = description;

    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(), null, errorCode.getDescription());
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200, "success", data, null);
    }

    public static <T> BaseResponse<T> fail(int code, String message, String description) {
        return new BaseResponse<>(code, message, null, description);
    }

    public static <T> BaseResponse<T> fail(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(), message, null, description);
    }

    public static <T> BaseResponse<T> fail(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(), errorCode.getMessage(), null, errorCode.getDescription());
    }
}
