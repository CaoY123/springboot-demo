package com.mine.spring.context.clazz;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author CaoYang
 * @create 2023-04-22-2:09 AM
 * @description 用于学习 Spring中的 ApplicationContextAware
 */
@Service
public class MyContextFirst implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public void doSomething() {
        // 这么做能拿到 bean 的名到 bean 对象的映射，包括Person及其子类的bean
        Map<String, Person> beansOfType = applicationContext.getBeansOfType(Person.class);
        for (Map.Entry<String, Person> entry : beansOfType.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

@Component
@Data
class Person {
    private String name;
    private int age;
}

@Component
@Data
class Student extends Person{
    private String stuId;
}

@Component
@Data
class Teacher extends Person {
    private String subject;
}

