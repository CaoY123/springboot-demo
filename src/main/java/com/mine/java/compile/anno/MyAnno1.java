package com.mine.java.compile.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author CaoY
 * @date 2023-08-02 14:33
 * @description 自定义的注解1
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnno1 {
    String name() default "";// 默认值不能为 null，否则编译会不通过，String 作为元素
    int age() default 25; // 基本类型作为元素
    Class clazz() default void.class; // Class 对象
    Character character() default Character.STUDENT; // 枚举作为元素
    int[] grades() default {}; // 数组作为元素
    // AnnoA annoA(); // 不能将类对象作为注解中的元素
}
