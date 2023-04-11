package com.mine.generic;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CaoY
 * @date 2023-04-11 23:18
 * @description 泛型测试4 - 与泛型数组初始化相关
 */
public class GenericTest4 {

    @Test
    public void test1() {
        // Java不允许支持数组泛型初始化 - 目前还是有点不理解，可以把下面的代码放开理解
//        List<String>[] lsa = new List<String>[10]; // Not really allowed.
//        Object o = lsa;
//        Object[] oa = (Object[]) o;
//        List<Integer> li = new ArrayList<Integer>();
//        li.add(new Integer(3));
//        oa[1] = li; // Unsound, but passes run time store check
//        String s = lsa[1].get(0); // Run-time error ClassCastException.

        // 采用通配符初始化泛型数组是允许的
        List<?>[] lsa = new List<?>[10]; // OK, array of unbounded wildcard type.
        Object o = lsa;
        Object[] oa = (Object[]) o;
        List<Integer> li = new ArrayList<Integer>();
        li.add(new Integer(3));
        oa[1] = li; // Correct.
        Integer i = (Integer) lsa[1].get(0); // OK
    }

    // 使用反射的方式传入数组的元素的类型和数组的规模来 优雅 的初始化泛型数组
    @Test
    public void test2() {
        ArrayWithTypeToken<Integer> arrayToken = new ArrayWithTypeToken<>(Integer.class, 100);
        Integer[] array = arrayToken.create();
    }
}

class ArrayWithTypeToken<T> {
    private T[] array;

    public ArrayWithTypeToken(Class<T> type, int size) {
        // Array 的 java.lang.reflect的类
        array = (T[]) Array.newInstance(type, size);
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return array[index];
    }

    public T[] create() {
        return array;
    }
}
