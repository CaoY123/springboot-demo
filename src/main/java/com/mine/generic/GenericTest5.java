package com.mine.generic;

import org.junit.Test;

/**
 * @author CaoY
 * @date 2023-04-11 23:36
 * @description 泛型测试5 静态部分和泛型
 */
public class GenericTest5 {

    @Test
    public void test1() {

    }
}

class TestStatic<T> {
    // 泛型对象的实例化是在定义对象的时候指定的，
    // 而静态变量和静态方法不需要使用对象来调用，对象都没有创建，
    // 所以无法确定这个泛型参数是何种类型
//    public static T one; // 编译错误
//    public static T show(T one) {return null;} // 编译错误

    // 这是允许的，这里的T是自己在方法中定义的
    public static <T> T show(T one) {
        return null;
    }

}