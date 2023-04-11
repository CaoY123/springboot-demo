package com.mine.generic;

import org.junit.Test;

import java.util.Date;

/**
 * @author CaoY
 * @date 2023-04-11 22:39
 * @description 泛型测试3
 */
public class GenericTest3 {

    @Test
    public void test1() {
        DateInter dateInter = new DateInter();
        dateInter.setValue(new Date());
//        dateInter.setValue(new Object());// 编译错误
    }

    @Test
    public void test2() {

    }

}

// 父类泛型类
class Pair<T> {

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}

// 子类
class DateInter extends Pair<Date> {

    @Override
    public void setValue(Date value) {
        super.setValue(value);
    }

    @Override
    public Date getValue() {
        return super.getValue();
    }
}

class ClassT<T> {

    T tObj;

    public ClassT() {
        // java编译期没法确定泛型参数化类型，也就找不到对应的类字节码文件
//        this.tObj = new T();// 编译报错
    }

    // 通过反射来实行泛型的实例化
    public static <T> T newTClass(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        T obj = clazz.newInstance();
        return obj;
    }
}
