package com.mine.domain;

import com.mine.spring.exception.enums.RetCodeEnum;

/**
 * @author CaoYang
 * @create 2023-04-23-4:36 PM
 * @description 响应结果数据结构
 */
public class RetResult<T> {
    private RetCodeEnum code;
    private String message;
    private T result;

    public RetResult() {

    }

    public RetResult(RetCodeEnum code, String message) {
        this.code = code;
        this.message = message;
    }

    public RetResult(RetCodeEnum code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RetResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
