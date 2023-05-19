package com.mine.java.collections;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author CaoYang
 * @create 2023-04-28-11:55 AM
 * @description ArrayList测试类2
 */
public class ArrayListTest2 {

    @Test
    public void test1() {
        ArrayList<Integer> list1 = new ArrayList<>();
        list1.addAll(Arrays.asList(1, 2, 3, 4, 5, 6));
//        System.out.println(list1);
        Iterator<Integer> iterator = list1.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

}
