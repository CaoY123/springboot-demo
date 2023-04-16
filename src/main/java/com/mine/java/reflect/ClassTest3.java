package com.mine.java.reflect;

import org.junit.Test;

/**
 * @author CaoYang
 * @create 2023-04-17-1:14 AM
 * @description
 */
public class ClassTest3 {
    private class Inner {

    }

    @Test
    public void test1() throws ClassNotFoundException {
        String[] args = new String[10];
        //普通类
        System.out.println(ClassTest3.class.getSimpleName()); //ClassTest3
        System.out.println(ClassTest3.class.getName()); //com.mine.reflect.ClassTest3
        System.out.println(ClassTest3.class.getCanonicalName()); //com.mine.reflect.ClassTest3
        //内部类
        System.out.println(Inner.class.getSimpleName()); //Inner
        System.out.println(Inner.class.getName()); //com.mine.reflect.ClassTest3$Inner
        System.out.println(Inner.class.getCanonicalName()); //com.mine.reflect.ClassTest3.Inner
        //数组
        System.out.println(args.getClass().getSimpleName()); //String[]
        System.out.println(args.getClass().getName()); //[Ljava.lang.String;
        System.out.println(args.getClass().getCanonicalName()); //java.lang.String[]
        // 我们不能用getCanonicalName去加载类对象，必须用getName，因为getName()获取的是jvm中的Class表示，
        // 而getCanonicalName则是包表示，易于查看和理解
//        Class.forName(Inner.class.getCanonicalName()); // 报错
        Class.forName(Inner.class.getName());
    }
}
