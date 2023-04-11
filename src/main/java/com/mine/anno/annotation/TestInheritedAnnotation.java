package com.mine.anno.annotation;

import java.lang.annotation.*;

/**
 * @author CaoY
 * @date 2023-04-12 0:46
 * @description 自定义注解
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TestInheritedAnnotation {
    String[] values();
    int number();
}
