package com.mine.spring.exception.domain;

import com.mine.spring.exception.enums.RetCodeEnum;

import java.util.Objects;

/**
 * @author CaoYang
 * @create 2023-04-23-3:10 PM
 * @description 处理异常的统一响应体
 */
public class ErrorResponse {
    private RetCodeEnum code;
    private String message;

    public ErrorResponse() {

    }

    public ErrorResponse(RetCodeEnum code, String message) {
        this.code = code;
        this.message = message;
    }

    public RetCodeEnum getCode() {
        return code;
    }

    public void setCode(RetCodeEnum code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
