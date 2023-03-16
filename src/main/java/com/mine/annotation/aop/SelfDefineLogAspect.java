package com.mine.annotation.aop;

import com.mine.annotation.anno.SelfDefineLog;
import com.mine.annotation.entity.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;

/**
 * @author CaoYang
 * @create 2023-03-16-10:20 AM
 * @description
 */
@Component
@Aspect
public class SelfDefineLogAspect {

    @Pointcut("@annotation(com.mine.annotation.anno.SelfDefineLog)")
    private void pointcut() {}

    @Before("pointcut() && @annotation(logger)")
    public void beforeAdvice(JoinPoint joinPoint, SelfDefineLog logger) {
//        System.out.println("=== SelfDefineLog 日志的内容为：【" + logger.value() + "】 ===");
        Signature signature = joinPoint.getSignature();
//        System.out.println("注解作用的方法名：" + signature.getName());
//        System.out.println("所在类的简单类名：：" + signature.getDeclaringType().getSimpleName());
//        System.out.println("所在类的完整类名：：" + signature.getDeclaringType());
//        System.out.println("目标方法的声明类型：" + Modifier.toString(signature.getModifiers()));
        System.out.println("【"
        + signature.getDeclaringType().getSimpleName()
        + "】【" + signature.getName()
        + "】-日志内容-【" + logger.value() + "】");
    }

    @Around("pointcut() && @annotation(logger)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, SelfDefineLog logger) {
        System.out.println("["
                + joinPoint.getSignature().getDeclaringType().getSimpleName()
                + "][" + joinPoint.getSignature().getName()
                + "]-日志内容-[" + logger.value() + "]");

        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Integer) {
                args[i] = (Integer) args[i] - 1;
                break;
            }
        }

        Object result = null;

        try {
            result = joinPoint.proceed(args);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (result instanceof User) {
            User user = (User) result;
            user.setId(user.getId() + 1);
            return user;
        }
        return result;
    }
}
