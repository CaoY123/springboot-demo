package com.mine.java.io.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author CaoY
 * @date 2023-06-17 16:53
 * @description 雇员类，继承自 User
 */
@Data
public class Employee implements Serializable {
    private Integer id;
    private String name;
    private String company;// 所在公司
    private String position;// 岗位

    public Employee() {

    }

    public Employee(Integer id, String name, String company, String position) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.position = position;
    }
}
