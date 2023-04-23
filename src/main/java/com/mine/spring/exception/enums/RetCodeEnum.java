package com.mine.spring.exception.enums;

import lombok.Data;

/**
 * @author CaoYang
 * @create 2023-04-23-3:16 PM
 * @description 响应吗枚举
 */
public enum RetCodeEnum {

    SUCCESS("000000", "成功"),
    FAILURE("555555", "失败"),
    UNCERTAIN("111111", "未知"),
    ;

    private String code;
    private String desc;

    RetCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
