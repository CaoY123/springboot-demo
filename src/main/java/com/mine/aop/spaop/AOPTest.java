package com.mine.aop.spaop;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.mine.aop.spaop.domain.User;

/**
 * @author CaoYang
 * @create 2023-03-16-1:48 PM
 * @description Spring 中 AOP 的测试类
 */
@SpringBootApplication
public class AOPTest {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(AOPTest.class, args);
        User user = context.getBean("user", User.class);
//        user.add();
//        user.add(1);
        user.sub();
    }

}
