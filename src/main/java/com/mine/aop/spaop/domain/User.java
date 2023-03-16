package com.mine.aop.spaop.domain;

import org.springframework.stereotype.Component;

//被增强的类
@Component
public class User {
    //预增强的方法
    public void add(){
        System.out.println("add User......");
    }

    public void add(int a) {
        System.out.println("add User one parameter ...");
    }

    public void sub() {
        System.out.println("sub User function ...");
    }
}
