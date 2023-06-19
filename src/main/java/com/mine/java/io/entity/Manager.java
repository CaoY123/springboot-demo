package com.mine.java.io.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author CaoY
 * @date 2023-06-17 16:54
 * @description 经理类，继承自 Employee
 */
@Data
@ToString(callSuper = true)
public class Manager extends Employee{

    private Employee secretary;// 秘书
    private String department;// 所在部门

    private Manager() {

    }

    public Manager(Employee secretary, String department) {
        super();
        this.secretary = secretary;
        this.department = department;
    }

    public Manager(Integer id, String name,
            String company, String position,
                   Employee secretary, String department) {
        super(id, name, company, position);
        this.secretary = secretary;
        this.department = department;
    }


}
