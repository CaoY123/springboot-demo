package com.mine.java.compile.anno;

import java.lang.annotation.*;

/**
 * 动作监听器的自定义注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionListenerFor
{
   String source();
}