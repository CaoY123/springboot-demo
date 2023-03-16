package com.mine.aop.spaop.proxy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Creator CaoY
 * @Description User 的代理类对象，下面的各方法分别体现了在不同的add()调用时候的执行
 * 下面采用了切入点表达式的方式进行，可以看到，这么做会有多个切入点表达式，如果有修改的话需要改多处，较为麻烦
 * 因此可以提取切入点，用 Pointcut()
 * 注：对于下面的 Order 来说：数值越小，优先级越高
 */
//增强的类
@Component
@Aspect    //生成代理对象
@Order(0)
public class UserProxy {

    // 切入点表达式，可以用于统一描述切入点
    @Pointcut("execution(* com.mine.aop.spaop.domain.User.add(..))")
    public void pointcut1() {}

    // 定义第二个切入点
    @Pointcut("execution(* com.mine.aop.spaop.domain.User.sub(..))")
    public void pointcut2() {}

    //前置通知 （运行前执行）
//    @Before("execution(* com.mine.aop.spaop.domain.User.add(..))")
    @Before("pointcut2()")
    public void before(){
        System.out.println("before...");
    }

    // 后置通知 （运行后执行）
//    @After("execution(* com.mine.aop.spaop.domain.User.add(..))")
//    @After("pointcut1()")
//    @After("pointcut2()")
    public void afterFunction() {
        System.out.println("After...");
    }

    // 环绕通知（运行前后执行）
    @Around("execution(* com.mine.aop.spaop.domain.User.add(..))")
    public void aroundFunction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("========Around Before========");
        proceedingJoinPoint.proceed();
        System.out.println("========Around After========");
    }

    // 异常通知 （报错后执行）
    @AfterThrowing("execution(* com.mine.aop.spaop.domain.User.add(..))")
    public void AfterThrowingFunction() {
        System.out.println("########### 运行出错后执行 ###########");
    }

    // 后置通知（运行后执行）
    @AfterReturning("execution(* com.mine.aop.spaop.domain.User.*(..))")
    public void afterReturning() {
        System.out.println("========== 程序跑完后执行 ==========");
    }
}
