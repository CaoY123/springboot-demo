package com.mine.oop;

import lombok.Data;
import org.junit.Test;

/**
 * @author CaoYang
 * @create 2023-04-21-3:47 PM
 * @description 面向对象的测试
 */
public class ObjectOrientedTest {

    // 在默认情况下，调用默认的构造函数会对某个类属性赋值为null，这启示我们当调用类默认构造函数构造类如下面AA一样的对象
    // 时，一定不要使用其getAa()方法获得其对应的aa值，因为一定为null，若再使用这个null值则会使代码抛出NullPointerException
    @Test
    public void test1() {
        AA aa = new AA();
        System.out.println(aa);
    }

    // 下面的测试用例是对test1的纠正，如下面的用法，就不会在操作aa1属性时抛出NullPointerException，还能保证后续对
    // aa1的修改
    @Test
    public void test2() {
        AA aa = new AA();
        AA1 aa1 = new AA1();
        aa.setAa1(aa1);

        AA1 aa11 = aa.getAa1();
        aa1.setType("默认的");

        System.out.println(aa);
    }

}

@Data
class AA {
    private String name;
    private int age;
    private AA1 aa1;
}

@Data
class AA1 {
    private String type;
}