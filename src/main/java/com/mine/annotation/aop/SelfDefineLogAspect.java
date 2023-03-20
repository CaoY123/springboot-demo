package com.mine.annotation.aop;

import com.mine.annotation.anno.SelfDefineLog;
import com.mine.annotation.entity.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;

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

    @After("pointcut() && @annotation(logger)")
    public void AfterAdvice(JoinPoint joinPoint, SelfDefineLog logger) {
//        System.out.println("=== SelfDefineLog 日志的内容为：【" + logger.value() + "】 ===");
        // 获取作用对象的签名，此处为作用在方法上的签名
        Signature signature = joinPoint.getSignature();
//        System.out.println("注解作用的方法名：" + signature.getName());
//        System.out.println("所在类的简单类名：：" + signature.getDeclaringType().getSimpleName());
//        System.out.println("所在类的完整类名：：" + signature.getDeclaringType());
//        System.out.println("目标方法的声明类型：" + Modifier.toString(signature.getModifiers()));

//        System.out.println("【"
//        + signature.getDeclaringType().getSimpleName()
//        + "】【" + signature.getName()
//        + "】-日志内容-【" + logger.value() + "】");

        // 下面代码用于测试反射的相关内容
        // 获取使用注解的方法所在的类
        Class<?> clazz = signature.getDeclaringType();
        // 获取使用该注解的方法名
        String methodName = signature.getName();
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();

        Method method = null;
        try {
//            Class[] parameterTypes = ((MethodSignature) signature).getParameterTypes();
//            method = clazz.getDeclaredMethod(methodName, parameterTypes);
             method = ((MethodSignature)signature).getMethod();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String res = null;
        if (method != null) {
            System.out.println("标注的方法：" + method);
            // 需要 jdk 版本在1.8及其以上
            int index = 0;
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
//                System.out.println(parameter.getName() + " -> " + parameter.getType());
                Class<?> type = parameter.getType();

                res = traversalToFind(args[index], type, logger.value());
                if (res != null) {
                    System.out.println("accountId -> " + res);

                    break;
                }

                index++;
            }
        }
    }

    // 递归遍历找字段
    private <T> String traversalToFind(Object obj, Class<T> clazz, String name) {
        if (clazz == null) {
            return null;
        }
        T tObj = clazz.cast(obj);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(name)) {
                System.out.println("字段名：" + field.getName());
                Method method = null;
                String res = null;
                try {
                    method = clazz.getDeclaredMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                    res = (String) method.invoke(tObj);
                } catch (Exception e) {
                    throw new RuntimeException("get方法出错");
                }
                return res;
            }
        }
        return null;
    }

    //    @Around("pointcut() && @annotation(logger)")
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
