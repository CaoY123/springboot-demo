package com.mine.spring.exception;

import com.mine.spring.exception.domain.ErrorResponse;
import com.mine.spring.exception.enums.RetCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author CaoYang
 * @create 2023-04-23-3:04 PM
 * @description SpringMVC 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException (RuntimeException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(RetCodeEnum.FAILURE);
        errorResponse.setMessage(exception.getMessage());
        return errorResponse;
    }
}
