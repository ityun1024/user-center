package com.yun.usercenter.exception;

import com.yun.usercenter.common.BaseResponse;
import com.yun.usercenter.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> handleBusinessException(BusinessException e) {
        log.error("handleBusinessException:" + e.getMessage(), e);
        return BaseResponse.fail(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> handleRuntimeException(RuntimeException e) {
        log.error("handleRuntimeException:", e);
        return BaseResponse.fail(ErrorCode.SERVER_ERROR, e.getMessage(), "");
    }



}
