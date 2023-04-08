package com.mine.java.finalval;

import com.mine.java.finalval.entity.Dog;
import org.junit.Test;

/**
 * @author CaoY
 * @date 2023-04-08 15:16
 * @description final 有关测试
 */
public class FinalTest {

    @Test
    public void test1() {
        final int a = 1;
        System.out.println("a = " + a);
        final Dog dog = new Dog("大黄");
        System.out.println("dog -> " + dog.getName());
        // 下列语句报错，常量引用不能更换指向的位置
//        dog = new Dog("二哈");
        dog.setName("二哈");
        // 常量引用可以修改所指位置上的值
        System.out.println("dog -> " + dog.getName());
    }
}
