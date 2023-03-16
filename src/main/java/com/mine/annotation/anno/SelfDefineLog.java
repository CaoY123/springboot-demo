package com.mine.annotation.anno;

import java.lang.annotation.*;

/**
 * @author CaoYang
 * @create 2023-03-16-10:12 AM
 * @description 自定义的记录日志的注解
 * 参考链接：https://blog.csdn.net/qq_37435078/article/details/90523309
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SelfDefineLog {

    String value() default "";

}
