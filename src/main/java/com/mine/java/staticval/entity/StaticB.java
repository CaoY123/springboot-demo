package com.mine.java.staticval.entity;

/**
 * @author CaoY
 * @date 2023-04-08 15:32
 * @description StaticA的子类，用于反映初始化时子父类的各种部分的初始化顺序
 */
public class StaticB extends StaticA{

    private static String valB1;

    static {
        valB1 = "子类的静态变量...";
        System.out.println("子类的静态变量：" + valB1);
        System.out.println("子类的静态代码块...");
    }

    private String valB2 = "子类的实例变量...";

    public StaticB() {
        System.out.println("子类的实例变量：" + valB2);
        System.out.println("子类的构造方法...");
    }

}
