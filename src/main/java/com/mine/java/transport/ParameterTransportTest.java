package com.mine.java.transport;

import com.mine.java.transport.entity.Dog;
import org.junit.Test;

/**
 * @author CaoY
 * @date 2023-04-08 14:49
 * @description Java参数传递参数 - 值传递的示例演示
 */
public class ParameterTransportTest {

    @Test
    public void test1() {

        Dog dog = new Dog("A");
        System.out.println(dog.getObjectAddress()); // Dog@4554617c
        func(dog);
        System.out.println(dog.getObjectAddress()); // Dog@4554617c
        System.out.println(dog.getName());          // A
    }

    private void func(Dog dog) {
        System.out.println("进入func(): ");
        System.out.println(dog.getObjectAddress()); // Dog@4554617c
        dog = new Dog("B");
        System.out.println(dog.getObjectAddress()); // Dog@74a14482
        System.out.println(dog.getName());          // B
        System.out.println("出来func(): ");
    }
}
