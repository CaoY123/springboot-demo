package com.mine.stream.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.util.Map;

/**
 * @author CaoYang
 * @create 2023-04-07-5:50 PM
 * @description
 */
@Builder
@Getter
@ToString
public class MyEntry {

    private String key;
    private Object entry;

    private Map<String, Object> dataMap;

    // 与@Builder注解兼容提供一个无参构造器，为了后续的反序列化的方便
    @Tolerate
    public MyEntry() {

    }

    // 全参构造器
    public MyEntry(String key, Object entry, Map<String, Object> dataMap) {
        this.key = key;
        this.entry = entry;
        this.dataMap = dataMap;
    }
}
