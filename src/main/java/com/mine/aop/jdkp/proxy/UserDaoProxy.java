package com.mine.aop.jdkp.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

//创建代理对象代码
public class UserDaoProxy implements InvocationHandler{

    //创建的是谁的代理对象就把谁传递过来
    private Object obj;

    //这里采用有参构造传递的形式
    public UserDaoProxy(Object obj){
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //方法执行前
        //使用method.getName()可以获得方法名
        //args是传入的参数
        System.out.println(method.getName() + "方法传入的参数为" + Arrays.toString(args));
        System.out.println(method.getName() + "方法执行之前。。。");

        //方法执行，传入对象和参数
        //使用method.invoke执行方法，obj是对象，args是传入的参数
        Object result = method.invoke(obj, args);

        //方法执行之后
        System.out.println(method.getName() + "方法执行完毕后。。。");
        return result;
    }
}
