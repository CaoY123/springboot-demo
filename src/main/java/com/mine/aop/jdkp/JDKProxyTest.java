package com.mine.aop.jdkp;

import com.mine.aop.jdkp.dao.UserDao;
import com.mine.aop.jdkp.dao.impl.UserDaoImpl;
import com.mine.aop.jdkp.proxy.UserDaoProxy;

import java.lang.reflect.Proxy;

/**
 * @Creator CaoY
 * @Description  JDK代理 —— 学习 aop 的原理
 * 链接：https://blog.csdn.net/weixin_44741023/article/details/119569398
 * 可以看到，可以在不修改原来方法的基础上，对原来的方法进行增强
 */
public class JDKProxyTest {
    public static void main(String[] args) {
        //创建接口实现类代理对象
        Class[] interfaces = {UserDao.class};//可写多个
        UserDaoImpl userDao = new UserDaoImpl();
        UserDao dao = (UserDao) Proxy.newProxyInstance(JDKProxyTest.class.getClassLoader(), interfaces, new UserDaoProxy(userDao));
        //执行add方法
        int result = dao.add(1,2);
        System.out.println("The result is "+ result);
        //执行update方法
        String id = dao.update("hello, world!");
        System.out.println("The id is "+ id);
    }
}