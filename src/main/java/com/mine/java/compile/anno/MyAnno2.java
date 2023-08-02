package com.mine.java.compile.anno;

import java.lang.annotation.*;

/**
 * @author CaoY
 * @date 2023-08-02 15:47
 * @description 自定义的注解2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited // 会使得加在父类上的注解加到子类上
public @interface MyAnno2 {
    String value() default "我的自定义注解2";
}
