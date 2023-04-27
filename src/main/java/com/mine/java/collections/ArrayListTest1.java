package com.mine.java.collections;

import io.swagger.models.auth.In;
import org.junit.Test;

import java.util.*;

/**
 * @author CaoY
 * @date 2023-04-27 23:26
 * @description ArrayList测试
 */
public class ArrayListTest1 {

    // debug探究
    @Test
    public void test1() {
        // 初始化为默认的空数组
        ArrayList<Integer> list1 = new ArrayList<>();
        // 初次添加一个元素时：确保容量是否足够 -> （不够）扩容，先扩展为10 -> 开辟新空间
        // 确保大小足够后调用 Arrays.copyOf(elementData, newCapacity); 来将原数组中的元素
        // 复制到新开辟的大小为 newCapacity 的数组上，因此扩容是很耗性能的。
        list1.add(1);
        System.out.println(list1);
    }

    // debug探究
    @Test
    public void test2() {
        ArrayList<Integer> list1 = new ArrayList<>();
        list1.add(1);
        // 此种情况下不进行扩容操作，因为第一次添加元素分配的大小为 10 的数组是够用的
        list1.add(2);
        System.out.println(list1);
    }

    // debug探究
    @Test
    public void test3() {
        ArrayList<Integer> list1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list1.add(i + 1);
        }
        // 当添加的元素超过 10 时，会进行1.5倍的扩容
        list1.add(11);
        System.out.println(list1);
    }

    // debug探究
    @Test
    public void test4() {
        ArrayList<Integer> list1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list1.add(i + 1);
        }
        // 这里如果一次性添加多个元素，如果一次性添加的元素数在经过1.5倍扩容后仍然无法容纳，那么我们在这里就按照所需的
        // 最少的情况来扩容，如下，显然16个位置才够容纳，扩容1.5倍后不够容纳，我们直接扩容至16
        list1.addAll(Arrays.asList(11, 12, 13, 14, 15, 16));
        System.out.println(list1);
    }

    // 故意传递索引值为负看看反应 debug探究
    @Test
    public void test5() {
        ArrayList<Integer> list1 = new ArrayList<>();
        list1.addAll(Arrays.asList(1, 2, 3, 4, 5, 6));
        System.out.println(list1);
        // 看看其索引传负值的反应，能通过最初的校验，其本质抛出的异常是数组下标传负值时所报的异常
        list1.set(-1, 10);
        System.out.println(list1);
    }

    // 下面是对于在 ArrayList 中使用的一个拷贝操作的探究，感觉挺好用的
    @Test
    public void test6() {
        int[] arr1 = {1, 2, 3, 4, 5, 6};
        int[] arr2 = arr1.clone();
        System.out.println("arr1: " + Arrays.toString(arr1));
        System.out.println("arr2: " + Arrays.toString(arr2));
        System.out.println(arr1 == arr2);

        int[] arr3 = Arrays.copyOf(arr1, arr1.length + 1);
        System.out.println("arr3: " + Arrays.toString(arr3));
        System.out.println(arr1 == arr3);

        // Arrays.copyOf()实际调用的方法
        int[] arr4 = new int[arr1.length + 5];
        // 将arr1的从下标为2的元素开始的4个元素依次拷贝到arr4中下标0开始的位置，注意不要越界
        System.arraycopy(arr1, 2, arr4, 0, 4);
        System.out.println("arr4: " + Arrays.toString(arr4));
        System.out.println(arr1 == arr4);
    }

    // 突发奇想，既然 ArrayList 允许将null放进去，而且其add相关的方法也没有对null值进行检测和特殊处理，
    // 说明java允许这种操作
    @Test
    public void test7() {
        Integer num = (Integer) null;
        System.out.println(num);
    }

    // 探究ArrayList的克隆方法
    @Test
    public void test8() {
        ArrayList<Integer> list1 = new ArrayList<>();
        list1.addAll(Arrays.asList(1, 2, 3, 4, 5, 6));
        // ArrayList 的 clone() 本质上调用的还是：Arrays.copyOf(elementData, size)
        ArrayList<Integer> list2 = (ArrayList<Integer>) list1.clone();
        System.out.println("list1: " + list1);
        System.out.println("list2: " + list2);
        System.out.println(list1 == list2);
    }

    // 探究并发场景下 ArrayList 的迭代器的快速失败
    @Test
    public void test9() {
        // 创建一个 ArrayList
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        // 创建一个线程，尝试在遍历 ArrayList 时修改它
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.add("D");
            System.out.println("Item added");
        }).start();

        try {
            // 使用迭代器遍历 ArrayList
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                System.out.println("Current item: " + iterator.next());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException caught");
        }
    }
}
