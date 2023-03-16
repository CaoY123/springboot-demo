package com.mine.aop.spaop.proxy;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author CaoYang
 * @create 2023-03-16-2:15 PM
 * @description 为了测试 通知的优先级设置的效果
 * 注：数值越小，优先级越高
 */
@Component
@Aspect
@Order(1)
public class PersonProxy {

    @Before("execution(* com.mine.aop.spaop.domain.User.sub(..))")
    public void add() {
        System.out.println("Person Proxy add ...");
    }

}
