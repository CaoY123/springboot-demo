package com.mine.java.io.entity;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author CaoY
 * @date 2023-06-10 10:44
 * @description 用于辅助学习 Stream 的用户类
 */
@Data
public class User {
    private Integer id;
    private String name;
    private String gender;
    private Integer age;
    private LocalDate birthday;

    public User() {

    }

    public User(Integer id, String name, String gender, Integer age) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public User(Integer id, String name, String gender, Integer age, LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.birthday = birthday;
    }
}
