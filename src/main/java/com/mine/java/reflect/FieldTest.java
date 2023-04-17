package com.mine.java.reflect;

import java.lang.reflect.Field;

/**
 * @author CaoY
 * @date 2023-04-17 23:38
 * @description 反射字段类测试
 */
public class FieldTest {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException {
        Class<?> clazz = Class.forName("com.mine.java.reflect.Student");
        // getField()获取的字段名必须是public的，但是可以获得父类的字段
        Field age = clazz.getField("age");
        System.out.println("superField: " + age);
        System.out.println("===================================");

        // 获取所有修饰符为public的字段，包含父类字段
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            System.out.println("f: " + field);
        }
        System.out.println("========getDeclaredFields===================");
        // 获取private字段，注意不包含父类的字段
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println("declaredFields: " + declaredField.getDeclaringClass());
        }
        System.out.println("=======================================");

        Field field2 = clazz.getDeclaredField("desc");
        System.out.println("field2: " + field2);
    }
}

class Person{
    public int age;
    public String name;
    //省略set和get方法
}

class Student extends Person{
    public String desc;
    private int score;
    //省略set和get方法
}
