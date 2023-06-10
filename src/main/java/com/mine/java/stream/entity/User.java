package com.mine.java.stream.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author CaoY
 * @date 2023-06-10 10:44
 * @description 用于辅助学习 Stream 的用户类
 */
@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String gender;
    private Integer age;
}
