package com.mine.aop.jdkp.dao.impl;

import com.mine.aop.jdkp.dao.UserDao;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Override
    public int add(int a, int b) {
        System.out.println("add方法执行中...");
        return a + b;
    }

    @Override
    public String update(String id) {
        System.out.println("update方法执行中");
        return id;
    }
}
