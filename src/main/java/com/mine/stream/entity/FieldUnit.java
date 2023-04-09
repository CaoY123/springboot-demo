package com.mine.stream.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * @author CaoYang
 * @create 2023-04-09-2:41 PM
 * @description 某属性的对比结果
 */
@Getter
@Builder
@ToString
public class FieldUnit {
    // 属性名
    private String fieldName;
    // 来源值
    private Object source;
    // 目标值
    private Object target;

    @Tolerate
    public FieldUnit() {

    }
}
