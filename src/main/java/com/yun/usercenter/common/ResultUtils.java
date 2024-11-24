package com.yun.usercenter.common;

/**
 * 响应工具类
 *
 * @author yun
 */
public class ResultUtils {

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200, "success", data);
    }

    public static <T> BaseResponse<T> success(T data, String description) {
        return new BaseResponse<>(200, "success", data, description);
    }

    public static <T> BaseResponse<T> fail(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }
}
