package com.mine.java.staticval.entity;

/**
 * @author CaoY
 * @date 2023-04-08 15:25
 * @description 含有静态部分的类，主要用于探究其加载、初始化过程时候不同部分的执行顺序
 */
public class StaticA {

    private static String val1 = "静态变量...";

    static {
        // 该静态块若放在val1 的上面，则会报错，可见其与static变量的相对顺序与所处位置有关
        System.out.println("静态变量的值：" + val1);
        System.out.println("静态代码块1...");
    }

    {
        System.out.println("非静态代码块...");
    }

    private String val2 = "实例变量...";

    public StaticA() {
        System.out.println("实例变量：" + val2);
        System.out.println("构造函数代码...");
    }
}
